package com.mycila.sandbox.junit.runner;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentSuite extends Suite {
    public ConcurrentSuite(Class<?> klass) throws InitializationError {
        super(klass, new AllDefaultPossibilitiesBuilder(true) {
            @Override
            public Runner runnerForClass(Class<?> testClass) throws Throwable {
                List<RunnerBuilder> builders = Arrays.asList(
                        new RunnerBuilder() {
                            @Override
                            public Runner runnerForClass(Class<?> testClass) throws Throwable {
                                Concurrent annotation = testClass.getAnnotation(Concurrent.class);
                                if (annotation != null)
                                    return new ConcurrentJunitRunner(testClass);
                                return null;
                            }
                        },
                        ignoredBuilder(),
                        annotatedBuilder(),
                        suiteMethodBuilder(),
                        junit3Builder(),
                        junit4Builder());
                for (RunnerBuilder each : builders) {
                    Runner runner = each.safeRunnerForClass(testClass);
                    if (runner != null)
                        return runner;
                }
                return null;
            }
        });
    }
}
