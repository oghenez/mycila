package com.mycila.ujd;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Field;
import java.util.Vector;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaUJD {

    public static void premain(String agentArgs, Instrumentation instrumentation) throws Exception {
        agentmain(agentArgs, instrumentation);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Exception {

    }

    // only for testing purposes
    public static void main(String... args) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) sb.append(arg).append(";");
        agentmain(sb.toString(), new Instrumentation() {
            public void addTransformer(ClassFileTransformer transformer) {
            }

            public boolean removeTransformer(ClassFileTransformer transformer) {
                return false;
            }

            public boolean isRedefineClassesSupported() {
                return false;
            }

            public void redefineClasses(ClassDefinition[] definitions) throws ClassNotFoundException, UnmodifiableClassException {
            }

            public Class[] getAllLoadedClasses() {
                try {
                    ClassLoader cl = ClassLoader.getSystemClassLoader();
                    Field classes = ClassLoader.class.getDeclaredField("classes");
                    classes.setAccessible(true);
                    Vector<Class<?>> vector = (Vector<Class<?>>) classes.get(cl);
                    return vector.toArray(new Class[vector.size()]);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            public Class[] getInitiatedClasses(ClassLoader loader) {
                return new Class[0];
            }

            public long getObjectSize(Object objectToSize) {
                return 0;
            }
        });
    }
}
