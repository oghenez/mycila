/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.ujd.impl;

import com.mycila.ujd.api.Analyzer;
import com.mycila.ujd.api.JVM;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaUJDAnalyzer implements Analyzer {

    private final Instrumentation instrumentation;
    private final CountDownLatch stopped = new CountDownLatch(1);
    private final DefaultJVMUpdater updater = new DefaultJVMUpdater();

    public MycilaUJDAnalyzer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void close() {
        stopped.countDown();
    }

    public void awaitClose() throws InterruptedException {
        stopped.await();
    }

    public JVM getJVM() {
        return updater.get();
    }
}
