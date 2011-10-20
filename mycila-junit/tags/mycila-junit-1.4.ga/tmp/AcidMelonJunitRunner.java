package com.ovea.acidmelon.agent.testing.junit;

import com.ovea.acidmelon.agent.testing.AcidMelonFeature;
import com.ovea.acidmelon.agent.testing.AnnotationValueProvider;
import com.ovea.acidmelon.agent.testing.annotation.AcidMelonFeatures;
import com.ovea.acidmelon.agent.testing.annotation.Concurrent;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.rules.MethodRule;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class AcidMelonJunitRunner extends BlockJUnit4ClassRunner {

    private AnnotationValueProvider<AcidMelonFeature> features;

    public AcidMelonJunitRunner(Class<?> klass) throws InitializationError {
        super(klass);
        if (klass.isAnnotationPresent(Concurrent.class))
            setScheduler(new ConcurrentRunnerScheduler(klass));
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        final Statement base = super.classBlock(notifier);
        features = new AnnotationValueProvider<AcidMelonFeature>(getTestClass().getJavaClass(), AcidMelonFeatures.class, AcidMelonFeature.class);
        if (features.isEmpty()) return base;
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                List<Throwable> errors = new ArrayList<Throwable>();
                try {
                    for (AcidMelonFeature feature : features) {
                        feature.init(getTestClass().getJavaClass());
                        feature.beforeClass(getTestClass().getJavaClass());
                    }
                    base.evaluate();
                } catch (Throwable e) {
                    errors.add(e);
                } finally {
                    for (AcidMelonFeature feature : features)
                        try {
                            feature.afterClass(getTestClass().getJavaClass());
                        } catch (Throwable e) {
                            errors.add(e);
                        }
                }
                MultipleFailureException.assertEmpty(errors);
            }
        };
    }

    @Override
    protected List<MethodRule> rules(Object test) {
        List<MethodRule> rules = super.rules(test);
        rules.add(new AcidMelonFeatureMethodRule(features));
        return rules;
    }
}
