/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
 
#include <jvmpi.h>
#include <jni.h>
#include <jvmdi.h>
#include <string.h>
#include "org_codehaus_aspectwerkz_extension_hotswap_HotSwapClient.h"
//#include <jvmdi.hpp>

// global jvmpi interface pointer
static JVMPI_Interface *jvmpi_interface;

// max bytecode size (real size is around 20000)
// @todo should we use realloc / malloc stuff ?
#define MAX 50000

/**
 * Function for handling JVMPI event notification
 *
 * This is used for debug purpose only and is not called in normal mode
 * Looking at the output of _HOOK event shows HotSwap effect
 */
void notifyEvent(JVMPI_Event *event) {
	switch(event->event_type) {
		case JVMPI_EVENT_CLASS_LOAD:
			// debug - print class name
			fprintf(stdout, "AspectWerkz> CLASS_LOAD: %s\n", event->u.class_load.class_name);
		break;
		case JVMPI_EVENT_CLASS_LOAD_HOOK:
			// debug - print original bytecode ptr and size
			fprintf(stdout, "AspectWerkz> CLASS_LOAD_HOOK: %p len: %d\n", event->u.class_load_hook.class_data, event->u.class_load_hook.class_data_len);

			// copy original bytecode - this is mandatory
			int new_len = event->u.class_load_hook.class_data_len;
			event->u.class_load_hook.new_class_data_len = new_len;
			event->u.class_load_hook.new_class_data = (unsigned char*)event->u.class_load_hook.malloc_f(new_len);
			memcpy(event->u.class_load_hook.new_class_data, event->u.class_load_hook.class_data, new_len);
			
			// debug - dump initial bytecode
			FILE *f = fopen("aspectwerkz.dump", "wa");
			for (int i = 0; i < event->u.class_load_hook.class_data_len; i++) {
				fputc(event->u.class_load_hook.class_data[i], f);
			}
			fprintf(f, "\n___NEXT___\n");
			fclose(f);

			fflush(stdout);
			
			// deregister from the event
			// jvmpi_interface->DisableEvent(JVMPI_EVENT_CLASS_LOAD_HOOK, NULL);
		break;
	}                       
}


/**
 * JVMPI module entry point
 *
 * The modified java.lang.ClassLoader is build thru JNI call to ClassLoaderPatcher
 * and HotSwapped in the JVM.
 *
 * If module option is specified, use the given file instead as bytecode source
 * instead of building it thru JNI call. This is experimental.
 *
 * On win2000 java 1.4.1? -Xdebug must be turned on.
 */
extern "C" { 
JNIEXPORT jint JNICALL JVM_OnLoad(JavaVM *jvm, char *options, void *reserved) {
	fprintf(stdout, "AspectWerkz> initializing ...\n", options);

	//*********************************************************************
	// prepare the JVMPI
	if ((jvm->GetEnv((void **)&jvmpi_interface, JVMPI_VERSION_1)) < 0) {
		fprintf(stderr, "AspectWerkz> JVMPI prepare failed\n");
		return JNI_ERR;
	} 

	// initialize jvmpi interface with the function notifyEvent
	jvmpi_interface->NotifyEvent = notifyEvent;

	// debug - enable class load event notification
	//jvmpi_interface->EnableEvent(JVMPI_EVENT_CLASS_LOAD, NULL);

	// debug - enable class load hook
	//jvmpi_interface->EnableEvent(JVMPI_EVENT_CLASS_LOAD_HOOK, NULL);
	
	// debug VM init
	//jvmpi_interface->EnableEvent(JVMPI_EVENT_JVM_INIT_DONE, NULL);

	
	//*********************************************************************
	// prepare the JNI
	JNIEnv *jni;
	if ((jvm->GetEnv((void**)&jni, JNI_VERSION_1_1)) < 0) {
		fprintf(stderr, "AspectWerkz> JNI prepare failed\n");
		return JNI_ERR;
	}
	/*if ((jvm->AttachCurrentThread((void **)&jni, NULL)) < 0) {
		fprintf(stderr, "AspectWerkz> JNI prepare failed\n");
		return JNI_ERR;
	}*/

	//*********************************************************************
	// prepare the JVMDI
	JVMDI_Interface_1 *jvmdi;
	jint res = jvm->GetEnv((void**)&jvmdi, JVMDI_VERSION_1);
	if (res < 0) {
		fprintf(stderr, "AspectWerkz> JVMDI prepare failed [code %d %d]\n", res, JVMDI_ERROR_ACCESS_DENIED);
		return JNI_ERR;
	}
	/*if ( ! jvmdi::enabled() ) {
		fprintf(stderr, "AspectWerkz> JVMDI NOT enabled");
		return JNI_ERR;
	}*/
	
	/*
  Thread* thread = ThreadLocalStorage::thread();
  if (thread != NULL && thread->is_Java_thread()) {
    if (Threads::is_supported_jni_version_including_1_1(version)) {
      *(JNIEnv**)penv = ((JavaThread*) thread)->jni_environment();
      return JNI_OK;
    } else if (version == JVMPI_VERSION_1 || version == JVMPI_VERSION_1_1) {
      *penv = (void* )jvmpi::GetInterface_1(version);	// version 1.X support
      return JNI_OK; 
    } else if (version == JVMDI_VERSION_1) {
      if (jvmdi::enabled()) {
        *penv = (void *)jvmdi::GetInterface_1(vm);
        return JNI_OK;
      } else {
        return JVMDI_ERROR_ACCESS_DENIED;
      }
    } else {
      *penv = NULL;
      return JNI_EVERSION;
    }
  } else {
    *penv = NULL;
    return JNI_EDETACHED;
  }
	*/
	
	
	
	
	// debug - prints the JVMDI version
	//jint version;
	//jvmdiError err = jvmdi->GetVersionNumber(&version);
	//fprintf(stderr, "JVMDI> %d\n", version);

	// check for HotSwap capabilities without schema change
	JVMDI_capabilities capabilities;
	jvmdi->GetCapabilities(&capabilities);
	if (capabilities.can_redefine_classes != 1) {
		fprintf(stderr, "AspectWerkz> HotSwap not supported by JVM\n");
		return JNI_ERR;
	}


	JVMDI_class_definition target;
	target.clazz = jni->FindClass("java/lang/ClassLoader");

	if (options != NULL) {
		fprintf(stdout, "AspectWerkz> loading bytecode from file %s\n", options);
		FILE *fin = fopen(options, "rb");
		if (fin == NULL) {
			fprintf(stderr, "AspectWerkz> could not read boot file %s\n", options);
			return JNI_ERR;
		}
		unsigned char data[MAX];
		int data_len = fread(data, sizeof(unsigned char), MAX, fin);
		//fprintf(stdout, "JVMDI> read %d\n", data_len);
		fclose(fin);
	
		target.class_bytes = (jbyte*)data;
		target.class_byte_count = data_len;
	} else {
		jthrowable ex = NULL;
		jclass clp = jni->FindClass("org/codehaus/aspectwerkz/hook/ClassLoaderPatcher");
		if (jni->ExceptionOccurred() != NULL) {
			fprintf(stderr, "AspectWerkz> error in JNI call:\n");
			jni->ExceptionDescribe();
			return JNI_ERR;
		}
		jmethodID clpm = jni->GetStaticMethodID(clp, "getPatchedClassLoader", "(Ljava/lang/String;)[B");
		if (jni->ExceptionOccurred() != NULL) {
			fprintf(stderr, "AspectWerkz> error in JNI call:\n");
			jni->ExceptionDescribe();
			return JNI_ERR;
		}
		
		// handle custom ClassLoaderPreProcessor with -Daspectwerkz.classloader.clpreprocessor=<impl FQN>
		jclass sysC = jni->FindClass("java/lang/System");
		jmethodID sysM = jni->GetStaticMethodID(sysC, "getProperty", "(Ljava/lang/String;)Ljava/lang/String;");
		jstring ppName = (jstring) jni->CallStaticObjectMethod(sysC, sysM, jni->NewStringUTF("aspectwerkz.classloader.clpreprocessor"));
		jbyteArray newBytes;
		if (ppName != NULL) {
			fprintf(stdout, "AspectWerkz> using ClassLoaderPreProcessor %s\n", jni->GetStringUTFChars(ppName, JNI_FALSE));
			newBytes = (jbyteArray) jni->CallStaticObjectMethod(clp, clpm, ppName);
		} else {
			newBytes = (jbyteArray) jni->CallStaticObjectMethod(clp, clpm, jni->NewStringUTF("org.codehaus.aspectwerkz.hook.impl.ClassLoaderPreProcessorImpl"));
		}
		target.class_bytes = jni->GetByteArrayElements(newBytes, JNI_FALSE);
		target.class_byte_count = jni->GetArrayLength(newBytes);
	}
	
	// HotSwap
	jvmdiError err = jvmdi->RedefineClasses(1, &target);
	if (err == JVMDI_ERROR_NONE) {
		fprintf(stdout, "AspectWerkz> ..... done.\n");
	} else {
		fprintf(stderr, "AspectWerkz> HotSwap failed [code %d]\n", err);
		return JNI_ERR;
	}
	
	fflush(stdout);
	fflush(stderr);

	return JNI_OK;
}
}


/**
 * In process HotSwap
 */
extern "C" {
JNIEXPORT jint JNICALL Java_org_codehaus_aspectwerkz_extension_hotswap_HotSwapClient_hotswap(JNIEnv *env, jclass jclient, jstring jclassName, jclass joriginalClass, jbyteArray jnewBytes, jint jnewLength) {

	//*********************************************************************
	// debug - Java 2 C allocations
	//const char *className = env->GetStringUTFChars(jclassName, 0);
	//fprintf(stdout, "AspectWerkz> client called for %s\n", className);

	//*********************************************************************
	// prepare the JVMDI
	JavaVM *jvm;
	jint res = env->GetJavaVM(&jvm);
	if (res < 0) {
		fprintf(stderr, "AspectWerkz> JVM prepare failed [code %d %d]\n", res, JVMDI_ERROR_ACCESS_DENIED);
		return JNI_ERR;
	}	
	
	JVMDI_Interface_1 *jvmdi;
	res = jvm->GetEnv((void**)&jvmdi, JVMDI_VERSION_1);
	if (res < 0) {
		fprintf(stderr, "AspectWerkz> JVMDI prepare failed [code %d %d]\n", res, JVMDI_ERROR_ACCESS_DENIED);
		return JNI_ERR;
	}
	// debug - prints the JVMDI version
	//jint version;
	//jvmdiError err = jvmdi->GetVersionNumber(&version);
	//fprintf(stderr, "JVMDI> %d\n", version);

	// check for HotSwap capabilities without schema change
	JVMDI_capabilities capabilities;
	jvmdi->GetCapabilities(&capabilities);
	if (capabilities.can_redefine_classes != 1) {
		fprintf(stderr, "AspectWerkz> HotSwap not supported by JVM\n");
		return JNI_ERR;
	}
	
	//*********************************************************************
	// prepare the HotSwap
	JVMDI_class_definition target;
	target.clazz = joriginalClass;
	target.class_byte_count = jnewLength;
	target.class_bytes = env->GetByteArrayElements(jnewBytes, JNI_FALSE);

	//*********************************************************************
	// do the HotSwap
	jvmdiError err = jvmdi->RedefineClasses(1, &target);
	if (err == JVMDI_ERROR_NONE) {
		//fprintf(stdout, "AspectWerkz> ..... done.\n");
	} else {
		fprintf(stderr, "AspectWerkz> HotSwap failed [code %d]\n", err);
		return JNI_ERR;
	}
	
	//fflush(stdout);
	fflush(stderr);

	//*********************************************************************
	// Java 2 C de-allocations - TODO mem leak on failure
	//env->ReleaseStringUTFChars(jclassName, className);

	return JNI_OK;

}
}


