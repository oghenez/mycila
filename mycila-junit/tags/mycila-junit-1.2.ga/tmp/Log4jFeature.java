package com.ovea.acidmelon.agent.testing.feature;

import com.mycila.log.Loggers;
import com.ovea.acidmelon.agent.testing.AcidMelonFeatureAdapter;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Log4jFeature extends AcidMelonFeatureAdapter {
    @Override
    public void init(Class<?> testClass) throws Throwable {
        Loggers.useLog4j();
    }
}