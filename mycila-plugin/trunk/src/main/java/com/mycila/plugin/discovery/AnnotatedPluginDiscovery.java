package com.mycila.plugin.discovery;

import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.discovery.support.ResourcePatternResolver;
import com.mycila.plugin.util.ClassUtils;
import com.mycila.plugin.util.StringUtils;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotatedPluginDiscovery implements PluginDiscovery {

    private static final Logger LOGGER = Logger.getLogger(AnnotatedPluginDiscovery.class.getName());

    private final Class<? extends Annotation> annotationClass;
    private final String annotationClassDesc;
    private final ResourcePatternResolver resolver;
    private String[] includedPackages = new String[0];
    private String[] excludedPackages = {"java", "javax"};

    public AnnotatedPluginDiscovery() {
        this(Plugin.class, ClassUtils.getDefaultClassLoader());
    }

    public AnnotatedPluginDiscovery(Class<? extends Annotation> annotationClass, ClassLoader classLoader) {
        this.annotationClass = annotationClass;
        this.resolver = new ResourcePatternResolver(classLoader);
        this.annotationClassDesc = Type.getDescriptor(annotationClass);
    }

    public void includePackages(String... packages) {
        this.includedPackages = packages;
    }

    public void excludePackages(String... packages) {
        this.excludedPackages = packages;
    }

    @Override
    public Iterable<? extends Class<?>> scan() throws PluginDiscoveryException {
        setExclusions();
        ExecutorService executorService = Executors.newFixedThreadPool((int) (Runtime.getRuntime().availableProcessors() * 1.5));
        ExecutorCompletionService<Class<?>> completionService = new ExecutorCompletionService<Class<?>>(executorService);
        try {
            long count = submitTasks(completionService);
            SortedSet<Class<?>> plugins = collectResults(completionService, count);
            if (LOGGER.isLoggable(Level.CONFIG))
                LOGGER.config("Found " + plugins.size() + " plugins.");
            return Collections.unmodifiableSortedSet(plugins);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new PluginDiscoveryException(e.getCause(), annotationClass, includedPackages);
        } catch (IOException e) {
            throw new PluginDiscoveryException(e, annotationClass, includedPackages);
        } finally {
            executorService.shutdownNow();
        }
        return Collections.emptySet();
    }

    private SortedSet<Class<?>> collectResults(ExecutorCompletionService<Class<?>> completionService, long count) throws InterruptedException, ExecutionException {
        SortedSet<Class<?>> plugins = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        while (count-- > 0) {
            Class<?> c = completionService.take().get();
            if (c != null) plugins.add(c);
        }
        return plugins;
    }

    private long submitTasks(ExecutorCompletionService<Class<?>> completionService) throws IOException {
        long count = 0;
        if (includedPackages == null || includedPackages.length == 0) {
            if (LOGGER.isLoggable(Level.CONFIG))
                LOGGER.config("Scanning for classes annotated by @" + annotationClass.getSimpleName() + " in all packages, excluding packages " + StringUtils.arrayToCommaDelimitedString(excludedPackages) + "...");
            for (URL url : resolver.getResources("classpath*:**/*.class")) {
                count++;
                completionService.submit(new Processor(url));
            }
        } else {
            if (LOGGER.isLoggable(Level.CONFIG))
                LOGGER.config("Scanning for classes annotated by @" + annotationClass.getSimpleName() + " in packages " + StringUtils.arrayToCommaDelimitedString(includedPackages) + " excluding packages " + StringUtils.arrayToCommaDelimitedString(excludedPackages) + "...");
            for (String p : includedPackages) {
                for (URL url : resolver.getResources("classpath*:" + ClassUtils.convertClassNameToResourcePath(p) + "/**/*.class")) {
                    count++;
                    completionService.submit(new Processor(url));
                }
            }
        }
        return count;
    }

    private void setExclusions() {
        String[] prefixes = new String[excludedPackages.length];
        for (int i = 0; i < prefixes.length; i++)
            prefixes[i] = ClassUtils.convertClassNameToResourcePath(excludedPackages[i]) + "/";
        this.resolver.setExcludePrefixes(prefixes);
    }

    private class Processor implements Callable<Class<?>> {
        final URL url;

        private Processor(URL url) {
            this.url = url;
        }

        @Override
        public Class<?> call() throws Exception {
            InputStream is = null;
            try {
                is = url.openStream();
                ClassReader classReader = new ClassReader(is);
                ClassAnnotationVisitor visitor = new ClassAnnotationVisitor();
                classReader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                if ((visitor.access & Opcodes.ACC_PUBLIC) != 0 && visitor.annotations.contains(annotationClassDesc))
                    return resolver.getClassLoader().loadClass(ClassUtils.convertResourcePathToClassName(visitor.name));
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }
            }
            return null;
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
