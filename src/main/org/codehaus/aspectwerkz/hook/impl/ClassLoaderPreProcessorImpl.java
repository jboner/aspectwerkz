/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package org.codehaus.aspectwerkz.hook.impl;

import org.codehaus.aspectwerkz.hook.ClassLoaderPatcher;
import org.codehaus.aspectwerkz.hook.ClassLoaderPreProcessor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Instruments the java.lang.ClassLoader to plug in the Class PreProcessor mechanism using ASM. <p/>We are using a
 * lazy initialization of the class preprocessor to allow all class pre processor logic to be in system classpath and
 * not in bootclasspath. <p/>This implementation should support IBM custom JRE
 *
 * @author Chris Nokleberg
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur </a>
 */
public class ClassLoaderPreProcessorImpl implements ClassLoaderPreProcessor {

    private final static String CLASSLOADER_CLASS_NAME = "java/lang/ClassLoader";
    private final static String DEFINECLASS0_METHOD_NAME = "defineClass0";
    private final static String DEFINECLASS1_METHOD_NAME = "defineClass1";//For JDK 5
    private final static String DEFINECLASS2_METHOD_NAME = "defineClass2";//For JDK 5


    private static final String DESC_CORE = "Ljava/lang/String;[BIILjava/security/ProtectionDomain;";
    private static final String DESC_PREFIX = "(" + DESC_CORE;
    private static final String DESC_HELPER = "(Ljava/lang/ClassLoader;" + DESC_CORE + ")[B";

    private static final String DESC_BYTEBUFFER_CORE = "Ljava/lang/String;Ljava/nio/ByteBuffer;IILjava/security/ProtectionDomain;";
    private static final String DESC_BYTEBUFFER_PREFIX = "(" + DESC_BYTEBUFFER_CORE;
    private static final String DESC_BYTEBUFFER_HELPER = "(Ljava/lang/ClassLoader;" + DESC_BYTEBUFFER_CORE + ")[B";

    public ClassLoaderPreProcessorImpl() {
    }

    /**
     * Patch caller side of defineClass0
     * byte[] weaved = ..hook.impl.ClassPreProcessorHelper.defineClass0Pre(this, args..);
     * klass = defineClass0(name, weaved, 0, weaved.length, protectionDomain);
     *
     * @param classLoaderBytecode
     * @return
     */
    public byte[] preProcess(byte[] classLoaderBytecode) {
        try {
            ClassWriter cw = new ClassWriter(true);
            ClassLoaderVisitor cv = new ClassLoaderVisitor(cw);
            ClassReader cr = new ClassReader(classLoaderBytecode);
            cr.accept(cv, false);
            return cw.toByteArray();
        } catch (Exception e) {
            System.err.println("failed to patch ClassLoader:");
            e.printStackTrace();
            return classLoaderBytecode;
        }
    }

    private static class ClassLoaderVisitor extends ClassAdapter {
        public ClassLoaderVisitor(ClassVisitor cv) {
            super(cv);
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor cv = super.visitMethod(access, name, desc, signature, exceptions);
            Type[] args = Type.getArgumentTypes(desc);
            return new PreProcessingVisitor(cv, access, args);
        }
    }

    /**
     * @author Chris Nokleberg
     */
    private static class PreProcessingVisitor extends RemappingMethodVisitor {
        public PreProcessingVisitor(MethodVisitor mv, int access, Type[] args) {
            super(mv, access, args);
        }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            if ((DEFINECLASS0_METHOD_NAME.equals(name) || (DEFINECLASS1_METHOD_NAME.equals(name)))
                && CLASSLOADER_CLASS_NAME.equals(owner)) {
                Type[] args = Type.getArgumentTypes(desc);
                if (args.length < 5 || !desc.startsWith(DESC_PREFIX)) {
                    throw new Error("non supported JDK, native call not supported: " + desc);
                }
                // store all args in local variables
                int[] locals = new int[args.length];
                for (int i = args.length - 1; i >= 0; i--) {
                    mv.visitVarInsn(
                            args[i].getOpcode(Opcodes.ISTORE),
                            locals[i] = nextLocal(args[i].getSize())
                    );
                }
                for (int i = 0; i < 5; i++) {
                    mv.visitVarInsn(args[i].getOpcode(Opcodes.ILOAD), locals[i]);
                }
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "org/codehaus/aspectwerkz/hook/impl/ClassPreProcessorHelper",
                        "defineClass0Pre",
                        DESC_HELPER
                );
                mv.visitVarInsn(Opcodes.ASTORE, locals[1]);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, locals[0]); // name
                mv.visitVarInsn(Opcodes.ALOAD, locals[1]); // bytes
                mv.visitInsn(Opcodes.ICONST_0); // offset
                mv.visitVarInsn(Opcodes.ALOAD, locals[1]);
                mv.visitInsn(Opcodes.ARRAYLENGTH); // length
                mv.visitVarInsn(Opcodes.ALOAD, locals[4]); // protection domain
                for (int i = 5; i < args.length; i++) {
                    mv.visitVarInsn(args[i].getOpcode(Opcodes.ILOAD), locals[i]);
                }
            } else if (DEFINECLASS2_METHOD_NAME.equals(name) && CLASSLOADER_CLASS_NAME.equals(owner)) {
                Type[] args = Type.getArgumentTypes(desc);
                if (args.length < 5 || !desc.startsWith(DESC_BYTEBUFFER_PREFIX)) {
                    throw new Error("non supported JDK, bytebuffer native call not supported: " + desc);
                }
                // store all args in local variables
                int[] locals = new int[args.length];
                for (int i = args.length - 1; i >= 0; i--) {
                    mv.visitVarInsn(
                            args[i].getOpcode(Opcodes.ISTORE),
                            locals[i] = nextLocal(args[i].getSize())
                    );
                }
                for (int i = 0; i < 5; i++) {
                    mv.visitVarInsn(args[i].getOpcode(Opcodes.ILOAD), locals[i]);
                }
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "org/codehaus/aspectwerkz/hook/impl/ClassPreProcessorHelper",
                        "defineClass0Pre",
                        DESC_BYTEBUFFER_HELPER
                );
                mv.visitVarInsn(Opcodes.ASTORE, locals[1]);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, locals[0]); // name
                mv.visitVarInsn(Opcodes.ALOAD, locals[1]); // bytes
                mv.visitInsn(Opcodes.ICONST_0); // offset
                mv.visitVarInsn(Opcodes.ALOAD, locals[1]);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "Ljava/nio/Buffer;", "remaining", "()I");
                mv.visitVarInsn(Opcodes.ALOAD, locals[4]); // protection domain
                for (int i = 5; i < args.length; i++) {
                    mv.visitVarInsn(args[i].getOpcode(Opcodes.ILOAD), locals[i]);
                }
                // we should rebuild a new ByteBuffer...

            }
            super.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    /**
     * @author Chris Nokleberg
     */
    private static class State {
        Map locals = new HashMap();
        int firstLocal;
        int nextLocal;

        State(int access, Type[] args) {
            nextLocal = ((Opcodes.ACC_STATIC & access) != 0) ? 0 : 1;
            for (int i = 0; i < args.length; i++) {
                nextLocal += args[i].getSize();
            }
            firstLocal = nextLocal;
        }
    }

    /**
     * @author Chris Nokleberg
     */
    private static class IntRef {
        int key;

        public boolean equals(Object o) {
            return key == ((IntRef) o).key;
        }

        public int hashCode() {
            return key;
        }
    }

    /**
     * @author Chris Nokleberg
     */
    private static class RemappingMethodVisitor extends MethodAdapter {
        private State state;
        private IntRef check = new IntRef();


        public RemappingMethodVisitor(MethodVisitor v, int access, Type[] args) {
            super(v);
            state = new State(access, args);
        }

        public RemappingMethodVisitor(RemappingMethodVisitor wrap) {
            super(wrap.mv);
            this.state = wrap.state;
        }

        protected int nextLocal(int size) {
            int var = state.nextLocal;
            state.nextLocal += size;
            return var;
        }

        private int remap(int var, int size) {
            if (var < state.firstLocal) {
                return var;
            }
            check.key = (size == 2) ? ~var : var;
            Integer value = (Integer) state.locals.get(check);
            if (value == null) {
                IntRef ref = new IntRef();
                ref.key = check.key;
                state.locals.put(ref, value = new Integer(nextLocal(size)));
            }
            return value.intValue();
        }

        public void visitIincInsn(int var, int increment) {
            mv.visitIincInsn(remap(var, 1), increment);
        }

        public void visitLocalVariable(String name, String desc, String sig, Label start, Label end, int index) {
            mv.visitLocalVariable(name, desc, sig, start, end, remap(index, 0));
        }

        public void visitVarInsn(int opcode, int var) {
            int size;
            switch (opcode) {
                case Opcodes.LLOAD:
                case Opcodes.LSTORE:
                case Opcodes.DLOAD:
                case Opcodes.DSTORE:
                    size = 2;
                    break;
                default:
                    size = 1;
            }
            mv.visitVarInsn(opcode, remap(var, size));
        }

        public void visitMaxs(int maxStack, int maxLocals) {
            mv.visitMaxs(0, 0);
        }
    }

    public static void main(String args[]) throws Exception {
        ClassLoaderPreProcessor me = new ClassLoaderPreProcessorImpl();
        InputStream is = ClassLoader.getSystemClassLoader().getParent().getResourceAsStream(
                "java/lang/ClassLoader.class"
        );
        byte[] out = me.preProcess(ClassLoaderPatcher.inputStreamToByteArray(is));
        is.close();
        File dir = new File("_boot/java/lang/");
        dir.mkdirs();
        OutputStream os = new FileOutputStream("_boot/java/lang/ClassLoader.class");
        os.write(out);
        os.close();
    }
}