package com.mycila.testing.plugin.annotation;

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import com.mycila.testing.core.DefaultTestPlugin;
import com.mycila.testing.core.TestExecution;
import static com.mycila.testing.ea.ExtendedAssert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationTestPlugin extends DefaultTestPlugin {

    private static final Logger LOGGER = Loggers.get(AnnotationTestPlugin.class);

    @Override
    public void beforeTest(TestExecution testExecution) throws Exception {
        if(testExecution.method().isAnnotationPresent(Skip.class) || testExecution.context().test().hasAnnotation(Skip.class)) {
            LOGGER.debug("Skipping test method {0}.{1}", testExecution.method().getDeclaringClass().getName(), testExecution.method().getName());
            testExecution.setSkip(true);
        }
    }

    @Override
    public void afterTest(TestExecution testExecution) throws Exception {
        ExpectException expectException = testExecution.method().getAnnotation(ExpectException.class);
        if(expectException != null) {
            AssertException assertException = assertThrow(expectException.type());
            //noinspection StringEquality
            if(expectException.message() != ExpectException.NO_MESSAGE) {
                assertException.withMessage(expectException.message());
            } else //noinspection StringEquality
                if(expectException.containing() != ExpectException.NO_MESSAGE) {
                assertException.containingMessage(expectException.containing());
            }
        }
    }

}
