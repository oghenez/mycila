package com.mycila.sandbox.junit.runner;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentSuite extends Suite {
    public ConcurrentSuite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
        super(builder, classes);
    }
    public ConcurrentSuite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(builder, klass, suiteClasses);
    }
    public ConcurrentSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }
    public ConcurrentSuite(Class<?> klass, List<Runner> runners) throws InitializationError {
        super(klass, runners);
    }
    public ConcurrentSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(klass, suiteClasses);
    }
    
}
