/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.junit.rule;

import com.mycila.junit.concurrent.ConcurrentException;
import com.mycila.junit.concurrent.ConcurrentRunnerScheduler;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestExecution implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Times annotation = description.getAnnotation(Times.class);
                if (annotation == null)
                    base.evaluate();
                else {
                    int times = Math.max(0, annotation.value());
                    if (times > 0) {
                        if (!annotation.concurrent()) {
                            while (times-- > 0) {
                                base.evaluate();
                            }
                        } else {
                            ConcurrentRunnerScheduler scheduler = new ConcurrentRunnerScheduler(
                                description.getTestClass().getSimpleName() + (description.isTest() ? "#" + description.getMethodName() : ""), times);
                            final CountDownLatch go = new CountDownLatch(1);
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        go.await();
                                        base.evaluate();
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    } catch (Throwable throwable) {
                                        throw ConcurrentException.wrap(throwable);
                                    }
                                }
                            };
                            while (times-- > 0)
                                scheduler.schedule(runnable);
                            go.countDown();
                            try {
                                scheduler.finished();
                            } catch (ConcurrentException e) {
                                throw e.unwrap();
                            }
                        }
                    }
                }
            }
        };
    }
}
