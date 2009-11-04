import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.PathMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class SpringAnnotatedObjectFactory<T> extends AbstractFactoryBean implements ApplicationContextAware {

    private ApplicationContext context;
    @SuppressWarnings({"unchecked"})
    private Class<T>[] classes = new Class[0];

    public void setClasses(Class<T>[] classes) {
        this.classes = classes;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public Class<?> getObjectType() {
        return T[].class;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected Object createInstance() throws Exception {
        AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
        List<T> instances = new LinkedList<T>();
        for (Class<T> clazz : getClasses())
            instances.add((T) autowireCapableBeanFactory.createBean(clazz, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, true));
        return instances.toArray(new Object[instances.size()]);
    }

    public Class<T>[] getClasses() {
        return classes;
    }
}