/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycila.testing.plugin.annotation;

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.plugin.DefaultTestPlugin;
import com.mycila.testing.ea.Code;
import static com.mycila.testing.ea.ExtendedAssert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationTestPlugin extends DefaultTestPlugin {

    private static final Logger LOGGER = Loggers.get(AnnotationTestPlugin.class);

    @Override
    public void beforeTest(TestExecution testExecution) throws Exception {
        if (testExecution.method().isAnnotationPresent(Skip.class) || testExecution.context().introspector().hasAnnotation(Skip.class)) {
            LOGGER.debug("Skipping test method {0}.{1}", testExecution.method().getDeclaringClass().getName(), testExecution.method().getName());
            testExecution.setSkip(true);
        }
    }

    @Override
    public void afterTest(final TestExecution testExecution) throws Exception {
        ExpectException expectException = testExecution.method().getAnnotation(ExpectException.class);
        if (!testExecution.mustSkip() && expectException != null) {
            AssertException assertException = assertThrow(expectException.type());
            //noinspection StringEquality
            if (!ExpectException.NO_MESSAGE.equals(expectException.message())) {
                assertException.withMessage(expectException.message());
            } else //noinspection StringEquality
                if (!ExpectException.NO_MESSAGE.equals(expectException.containing())) {
                    assertException.containingMessage(expectException.containing());
                }
            try {
                assertException.whenRunning(new Code() {
                    public void run() throws Throwable {
                        if (testExecution.hasFailed()) {
                            throw testExecution.throwable();
                        }
                    }
                });
                testExecution.setThrowable(null);
            } catch (Throwable e) {
                testExecution.setThrowable(e);
            }
        }
    }

}
