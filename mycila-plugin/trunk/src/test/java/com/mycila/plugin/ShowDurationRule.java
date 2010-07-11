package com.mycila.plugin;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ShowDurationRule implements MethodRule {
    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long time = System.currentTimeMillis();
                try {
                    base.evaluate();
                } finally {
                    time = System.currentTimeMillis() - time;
                    System.out.println(method.getName() + ": " + time + "ms");
                }
            }
        };
    }
}
