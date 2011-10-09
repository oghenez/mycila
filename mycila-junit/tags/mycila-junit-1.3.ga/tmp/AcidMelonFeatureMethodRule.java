package com.ovea.acidmelon.agent.testing.junit;

import com.ovea.acidmelon.agent.testing.AcidMelonFeature;
import com.ovea.acidmelon.agent.testing.AnnotationValueProvider;
import com.ovea.acidmelon.agent.testing.annotation.AcidMelonFeatures;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AcidMelonFeatureMethodRule implements MethodRule {

    private AnnotationValueProvider<AcidMelonFeature> features;

    public AcidMelonFeatureMethodRule() {
    }

    public AcidMelonFeatureMethodRule(AnnotationValueProvider<AcidMelonFeature> features) {
        this.features = features;
    }

    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        if (features == null)
            features = new AnnotationValueProvider<AcidMelonFeature>(target.getClass(), AcidMelonFeatures.class, AcidMelonFeature.class);
        if (features.isEmpty()) return base;
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                List<Throwable> errors = new ArrayList<Throwable>();
                try {
                    for (AcidMelonFeature feature : features) {
                        feature.init(target, method.getMethod());
                        feature.before(target, method.getMethod());
                    }
                    base.evaluate();
                } catch (Throwable e) {
                    errors.add(e);
                } finally {
                    for (AcidMelonFeature feature : features)
                        try {
                            feature.after(target, method.getMethod());
                        } catch (Throwable e) {
                            errors.add(e);
                        }
                }
                MultipleFailureException.assertEmpty(errors);
            }
        };
    }
}
