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
package com.mycila.log.jdk.hook;

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import org.testng.annotations.Test;

import java.util.concurrent.CountDownLatch;
import java.util.logging.LogManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AsyncInvocationHandlerTest {

    @Test
    public void test_concurrency() throws Exception {

        LogManager.getLogManager().reset();
        LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/" + getClass().getSimpleName() + ".properties"));

        final Logger logger = Loggers.get(getClass());

        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch doneSignal = new CountDownLatch(100);

        for (int i = 0; i < 50; ++i) {
            final int i1 = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        startSignal.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                    for (int j = 0; j < 50; j++) {
                        logger.info("Hello {0} - {1} !", i1, j);
                    }
                    doneSignal.countDown();
                }
            }).start();
        }

        for (int i = 50; i < 100; ++i) {
            final int i1 = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        startSignal.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                    for (int j = 0; j < 50; j++) {
                        logger.error("Hello {0} - {1} !", i1, j);
                    }
                    doneSignal.countDown();
                }
            }).start();
        }

        startSignal.countDown();
        doneSignal.await();
        LogManager.getLogManager().reset();
    }

}
