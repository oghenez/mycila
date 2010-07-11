package com.mycila.plugin.discovery;

import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.discovery.support.ResourcePatternResolver;
import com.mycila.plugin.util.ClassUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotatedPluginDiscovery implements PluginDiscovery {

    private static final Object V = new Object();

    private final Class<? extends Annotation> annotationClass;
    private final String annotationClassDesc;
    private final ResourcePatternResolver resolver;
    private String[] includedPackages = new String[0];

    public AnnotatedPluginDiscovery() {
        this(Plugin.class, ClassUtils.getDefaultClassLoader());
    }

    public AnnotatedPluginDiscovery(Class<? extends Annotation> annotationClass, ClassLoader classLoader) {
        this.annotationClass = annotationClass;
        this.resolver = new ResourcePatternResolver(classLoader);
        this.resolver.setExcludePrefixes("java/", "javax/");
        this.annotationClassDesc = Type.getDescriptor(annotationClass);
    }

    public void includePackages(String... packages) {
        this.includedPackages = packages;
    }

    public void excludePackages(String... packages) {
        String[] prefixes = new String[packages.length];
        for (int i = 0; i < prefixes.length; i++)
            prefixes[i] = ClassUtils.convertClassNameToResourcePath(packages[i]) + "/";
        this.resolver.setExcludePrefixes(prefixes);
    }

    @Override
    public Iterable<? extends Class<?>> scan() throws PluginDiscoveryException {
        final ConcurrentMap<Class<?>, Object> plugins = new ConcurrentHashMap<Class<?>, Object>();
        final BlockingQueue<URL> toProcess = new LinkedBlockingQueue<URL>();
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        final Thread[] runners = new Thread[(int) (Runtime.getRuntime().availableProcessors() * 1.5)];

        try {
            for (int i = 0; i < runners.length; i++) {
                runners[i] = new Thread("AnnotatedPluginDiscovery-Thread-" + i) {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted() && failure.get() == null) {
                            try {
                                process(toProcess.take(), plugins);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            } catch (Exception e) {
                                failure.set(e);
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                    }
                };
                runners[i].start();
            }

            try {
                if (includedPackages == null || includedPackages.length == 0)
                    toProcess.addAll(Arrays.asList(resolver.getResources("classpath*:**/*.class")));
                else for (String p : includedPackages)
                    toProcess.addAll(Arrays.asList(resolver.getResources("classpath*:" + ClassUtils.convertClassNameToResourcePath(p) + "/**/*.class")));
            } catch (IOException e) {
                throw new PluginDiscoveryException(e, annotationClass, includedPackages);
            }

            while (!toProcess.isEmpty() && failure.get() == null)
                try {
                    process(toProcess.take(), plugins);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    failure.set(e);
                    break;
                }

            if (failure.get() != null)
                throw new PluginDiscoveryException(failure.get(), annotationClass, includedPackages);

        } finally {
            for (int i = 0; i < runners.length; i++) {
                if (runners[i] != null) {
                    runners[i].interrupt();
                    runners[i] = null;
                }
            }
        }

        return Collections.unmodifiableSet(plugins.keySet());
    }

    private void process(URL url, ConcurrentMap<Class<?>, Object> plugins) throws IOException, ClassNotFoundException {
        InputStream is = null;
        try {
            is = url.openStream();
            ClassReader classReader = new ClassReader(is);
            ClassAnnotationVisitor visitor = new ClassAnnotationVisitor();
            classReader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            if ((visitor.access & Opcodes.ACC_PUBLIC) != 0 && visitor.annotations.contains(annotationClassDesc))
                plugins.putIfAbsent(resolver.getClassLoader().loadClass(ClassUtils.convertResourcePathToClassName(visitor.name)), V);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException ignored) {
                }
        }
    }

    private static final class ClassAnnotationVisitor implements ClassVisitor {

        String name;
        int access;
        List<String> annotations = new ArrayList<String>(2);

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.name = name;
            this.access = access;
        }

        @Override
        public void visitSource(String source, String debug) {
        }

        @Override
        public void visitOuterClass(String owner, String name, String desc) {
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            annotations.add(desc);
            return null;
        }

        @Override
        public void visitAttribute(Attribute attr) {
        }

        @Override
        public void visitInnerClass(String name, String outerName, String innerName, int access) {
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            return null;
        }

        @Override
        public void visitEnd() {
        }
    }
}
