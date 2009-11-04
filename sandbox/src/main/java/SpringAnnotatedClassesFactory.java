import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.PathMatcher;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class SpringAnnotatedClassesFactory extends AbstractFactoryBean {

    private PathMatcher pathMatcher = new AntPathMatcher();
    private String[] packages = new String[0];
    @SuppressWarnings({"unchecked"})
    private Class<? extends Annotation>[] annotations = new Class[0];
    private String[] excludes = new String[0];
    private boolean onlyConcreteClasses = true;

    public void setOnlyConcreteClasses(boolean onlyConcreteClasses) {
        this.onlyConcreteClasses = onlyConcreteClasses;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public void setPackages(String[] packages) {
        this.packages = packages;
    }

    public void setAnnotations(Class<? extends Annotation>[] annotations) {
        this.annotations = annotations;
    }

    @Override
    public Class getObjectType() {
        return Class[].class;
    }

    @Override
    protected Object createInstance() throws Exception {
        List<Class<?>> classes = new LinkedList<Class<?>>();
        ClassPathScanningCandidateComponentProvider scanner = onlyConcreteClasses() ?
                new ClassPathScanningCandidateComponentProvider(false) {
                    @Override
                    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                        return beanDefinition.getMetadata().isConcrete();
                    }
                } :
                new ClassPathScanningCandidateComponentProvider(false) {
                    @Override
                    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                        return true;
                    }
                };
        Class<? extends Annotation>[] annotations = getAnnotations();
        for (Class<? extends Annotation> annotation : annotations)
            scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        for (String scannedPackage : getPackages()) {
            scannedPackage = scannedPackage.trim();
            if (logger.isTraceEnabled())
                logger.trace("Scanning package '" + scannedPackage + "' for classes annotated by: " + Arrays.toString(annotations) + "...");
            for (BeanDefinition def : scanner.findCandidateComponents(scannedPackage)) {
                String className = def.getBeanClassName();
                if (!isExcluded(className)) {
                    Class<?> clazz = ClassUtils.resolveClassName(className, Thread.currentThread().getContextClassLoader());
                    boolean matches = true;
                    for (Class<? extends Annotation> annot : annotations)
                        if (!clazz.isAnnotationPresent(annot)) {
                            matches = false;
                            break;
                        }
                    if (matches) {
                        classes.add(clazz);
                        if (logger.isTraceEnabled())
                            logger.trace("Found '" + clazz.getName() + "'");
                    }
                } else {
                    if (logger.isTraceEnabled())
                        logger.trace("Excluded: " + className);
                }
            }
        }
        return classes;
    }

    protected boolean isExcluded(String className) {
        className = className.replaceAll("\\.", "/");
        for (String pattern : getExcludes()) {
            if (getPathMatcher().match(pattern, className))
                return true;
        }
        return false;
    }

    public Class<? extends Annotation>[] getAnnotations() {
        return annotations;
    }

    public String[] getExcludes() {
        return excludes;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public boolean onlyConcreteClasses() {
        return onlyConcreteClasses;
    }

    public String[] getPackages() {
        return packages;
    }
}