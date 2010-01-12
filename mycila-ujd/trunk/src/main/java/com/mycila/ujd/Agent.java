package com.mycila.ujd;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Agent implements ClassFileTransformer {

    private static final AtomicBoolean loaded = new AtomicBoolean(false);
    private final Map<URL, Class<Void>> used = new HashMap<URL, Class<Void>>(500);
    private final Map<URL, Class<Void>> all = new HashMap<URL, Class<Void>>(500);
    private final WeakHashMap<ClassLoader, Class<Void>> cls = new WeakHashMap<ClassLoader, Class<Void>>(500);

    public Agent() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                display();
            }
        });
        new Thread() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    display();
                }
            }
        }.start();
    }

    private void display() {
        System.out.println(" === USED ===");
        for (URL url : used.keySet())
            System.out.println(" - " + url);
        Set<URL> unused = new HashSet<URL>(all.keySet());
        unused.removeAll(used.keySet());
        System.out.println(" === UNUSED ===");
        for (URL url : unused)
            System.out.println(" - " + url);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        add(className + ".class", loader);
        return classfileBuffer;
    }

    private void add(String resource, ClassLoader loader) {
        if (loader == null) loader = ClassLoader.getSystemClassLoader();
        if (!cls.containsKey(loader)) {
            if (loader instanceof URLClassLoader)
                for (URL url : ((URLClassLoader) loader).getURLs())
                    all.put(url, Void.TYPE);
            cls.put(loader, Void.TYPE);
        }
        final URL resUrl = loader.getResource(resource);
        if (resUrl == null) return;
        final StringBuilder url = new StringBuilder(resUrl.toExternalForm());
        try {
            if ("jar".equalsIgnoreCase(resUrl.getProtocol()))
                url.delete(0, 4).delete(url.indexOf(resource) - 2, url.length());
            else if ("file".equalsIgnoreCase(resUrl.getProtocol()))
                url.delete(url.indexOf(resource), url.length());
            else
                throw new UnsupportedOperationException("Cannot get container for resource [" + resource + "]. Unsupported URL protocol [" + resUrl.getProtocol() + "].");
            URL loc = new URL(url.toString());
            if (used.put(loc, Void.TYPE) == null)
                System.out.println("===> " + loc);
        }
        catch (MalformedURLException e) {
            throw new UnsupportedOperationException("Cannot get container for resource [" + resource + "]. Malformed URL [" + url + "].");
        }
    }

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {
        agentmain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        if (!loaded.getAndSet(true)) {
            final Agent agent = new Agent();
            instrumentation.addTransformer(agent);
            for (Class loadedClass : instrumentation.getAllLoadedClasses())
                if (loadedClass.isArray()) {
                    final String name = loadedClass.getName();
                    int pos = name.lastIndexOf('[');
                    int end = name.length();
                    if (name.charAt(pos + 1) == 'L') {
                        pos++;
                        end--;
                    }
                    agent.add(name.substring(pos + 1, end).replace('.', '/') + ".class", loadedClass.getClassLoader());
                } else
                    agent.add(loadedClass.getName().replace('.', '/') + ".class", loadedClass.getClassLoader());
        }
    }

}
