/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.weavebench;

import org.codehaus.aspectwerkz.transform.inlining.AsmHelper;
import org.codehaus.aspectwerkz.transform.AspectWerkzPreProcessor;
import org.codehaus.aspectwerkz.hook.impl.WeavingClassLoader;
import org.codehaus.aspectwerkz.definition.SystemDefinition;
import org.codehaus.aspectwerkz.definition.DeploymentScope;
import org.codehaus.aspectwerkz.definition.SystemDefinitionContainer;
import org.codehaus.aspectwerkz.definition.DefinitionParserHelper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassWriter;

import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class GenerateClasses implements Opcodes {

    private final static String DUMP_DIR = "_dump2";

    private final static String CLASS_NAME_PREFIX = "test/weavebench/Generated_";

    public int m_classCount;

    public int m_count;

    public GenerateClasses(int classCount, int methodCount) {
        m_classCount = classCount;
        m_count = methodCount;
    }

    public void generate() throws Throwable {
        for (int i = 0; i < m_classCount; i++) {
            ClassWriter cv = AsmHelper.newClassWriter(true);

            String className = CLASS_NAME_PREFIX + i;
            cv.visit(
                    AsmHelper.JAVA_VERSION,
                    ACC_PUBLIC + ACC_SUPER + ACC_SYNTHETIC,
                    className,
                    null,
                    Object.class.getName().replace('.', '/'),
                    new String[]{IGenerated.class.getName().replace('.', '/')}
            );

            MethodVisitor mv = cv.visitMethod(
                    ACC_PUBLIC,
                    "<init>",
                    "()V",
                    null,
                    new String[0]
            );
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);

            for (int m = 0; m < m_count; m++) {
                mv = cv.visitMethod(
                        (m==0)?ACC_PUBLIC:
                        ACC_PRIVATE,//TODO change to private to have wrapper, public for no wrappers
                        "method_" + m,
                        "()I",
                        null,
                        new String[0]
                );

                // call next method
                if (m != m_count-1) {
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitMethodInsn(INVOKEVIRTUAL, className, "method_" + (m+1), "()I");
                }
                AsmHelper.loadIntegerConstant(mv, m);
                mv.visitInsn(IRETURN);
                mv.visitMaxs(0, 0);
            }

            cv.visitEnd();

            AsmHelper.dumpClass(DUMP_DIR, className, cv);
        }
    }

    public static void main(String args[]) throws Throwable {

        Thread.sleep(3000);

        int CLASS_COUNT = 400;
        int METHOD_COUNT = 200;
        int JP_COUNT = (METHOD_COUNT*2 -1/*last method has no call jp*/ + 1/*init exec*/)*CLASS_COUNT;
        GenerateClasses me = new GenerateClasses(CLASS_COUNT, METHOD_COUNT);

        System.out.println("********* Bench for");
        System.out.println(" classes: " + CLASS_COUNT);
        System.out.println(" methods: " + METHOD_COUNT);
        System.out.println(" jps: " + JP_COUNT);
        System.out.println("*************************************");

        me.generate();

//        ClassLoader custom_1 = new URLClassLoader(
//                new URL[]{(new File(DUMP_DIR)).toURL()},
//                GenerateClasses.class.getClassLoader()
//        );
//        bench("No weaver hooked in", custom_1, CLASS_COUNT, 0);
//
//        ClassLoader custom_2 = new WeavingClassLoader(
//                new URL[]{(new File(DUMP_DIR)).toURL()},
//                GenerateClasses.class.getClassLoader()
//        );
//        bench("Weaver hooked in and match none", custom_2, CLASS_COUNT, 0);

        ClassLoader custom_3 = new WeavingClassLoader(
                new URL[]{(new File(DUMP_DIR)).toURL()},
                GenerateClasses.class.getClassLoader()
        );
        SystemDefinition sd = SystemDefinitionContainer.getVirtualDefinitionAt(custom_3);
        sd.addDeploymentScope(DeploymentScope.MATCH_ALL);
        DefinitionParserHelper.attachDeploymentScopeDefsToVirtualAdvice(sd);
        Set defs = new HashSet();
        defs.add(sd);
        SystemDefinitionContainer.deployDefinitions(custom_3, defs);
        bench("Weaver hooked in and match all", custom_3, CLASS_COUNT, JP_COUNT);

    }

    public static void bench(String label, ClassLoader loader, int classCount, int jpCount) throws Throwable {
        System.out.println("*************************************");
        System.out.print(label);
        System.out.println("  ");
        long t = System.currentTimeMillis();
        Class[] classes = new Class[classCount];
        for (int i = 0; i < classCount; i++) {
            classes[i] = Class.forName((CLASS_NAME_PREFIX+i).replace('/','.'), false, loader);
        }

        System.out.println("  Total load time = " + (System.currentTimeMillis() - t));

        long t2 = System.currentTimeMillis();
        for (int i = 0; i < classCount; i++) {
            Class gen = classes[i];
            Method m0 = gen.getMethod("method_0", new Class[]{});
            Object res = m0.invoke(gen.newInstance(), new Object[]{});
            //System.out.print(i);
        }
        System.out.println("");
        long execTimeNotWarmedUp = System.currentTimeMillis() - t2;
        System.out.println("  Exec time = " + execTimeNotWarmedUp);
        System.out.println("  Total time = " + (System.currentTimeMillis() - t));
        if (jpCount > 0)
            System.out.println("  Exec / jp ratio (ns) = " + execTimeNotWarmedUp * 1000 / jpCount);

        t2 = System.currentTimeMillis();
        for (int i = 0; i < classCount; i++) {
            Class gen = classes[i];
            Method m0 = gen.getMethod("method_0", new Class[]{});
            Object res = m0.invoke(gen.newInstance(), new Object[]{});
        }
        System.out.println("  Exec time warmed up = " + (System.currentTimeMillis() - t2));
    }


}
