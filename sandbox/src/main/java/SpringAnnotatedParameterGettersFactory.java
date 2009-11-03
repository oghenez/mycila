import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class SpringAnnotatedParameterGettersFactory extends SpringParameterGettersFactory implements ApplicationContextAware {

    private ApplicationContext context;
    private String[] scannedPackages = new String[]{"com.axacanada.product.getters.impl"};

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    protected ParameterGetters getDefaultMapping() {
        // prepare our map
        Map<Integer, ParameterGetter> getters = new TreeMap<Integer, ParameterGetter>();

        // get spring injector
        AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();

        // create the scanning tool which will scan for annotated classes in the packags we want
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Parameters.class));

        // for each package we want to scan into:
        for (String scannedPackage : getScannedPackages()) {
            scannedPackage = scannedPackage.trim();
            if (logger.isTraceEnabled())
                logger.trace("Scanning package '" + scannedPackage + "' for annotated instances of ParameterGetter");
            // find all the classes annotated under the package
            for (BeanDefinition def : scanner.findCandidateComponents(scannedPackage)) {
                Class<ParameterGetter> clazz;
                try {
                    clazz = ClassUtils.forName(def.getBeanClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                // if we really found an annotated ParameterGetter:
                if (ParameterGetter.class.isAssignableFrom(clazz)) {
                    // instanciate it and inject spring beans into
                    ParameterGetter getter = (ParameterGetter) autowireCapableBeanFactory.createBean(clazz, AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT, true);
                    int[] parameters = clazz.getAnnotation(Parameters.class).value();
                    if (logger.isTraceEnabled())
                        logger.trace("Found '" + clazz.getName() + "' for parameters " + Arrays.toString(parameters));
                    // then register this ParameterGetter for each parameter it supports
                    for (int parameter : parameters) {
                        getters.put(parameter, getter);
                    }
                }
            }
        }
        return ParameterGetters.from(getters);
    }

    public void setScannedPackages(String[] scannedPackages) {
        this.scannedPackages = scannedPackages;
    }

    protected String[] getScannedPackages() {
        return scannedPackages;
    }
}