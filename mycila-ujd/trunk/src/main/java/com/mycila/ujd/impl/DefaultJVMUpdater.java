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

import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JVMUpdater;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJVMUpdater implements JVMUpdater {

    private final JVM jvm;
    private final Instrumentation instrumentation;
    private long updateInterval = 20;
    private volatile Thread updater;
    private CountDownLatch stopped;

    public DefaultJVMUpdater(JVM jvm, Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        this.jvm = jvm;
    }

    public void update() {
        jvm.addClasses(instrumentation.getAllLoadedClasses());
    }

    public JVM get() {
        return jvm;
    }

    public void stop() {
        if (isRunning()) {
            updater.interrupt();
            updater = null;
            jvm.clear();
            stopped.countDown();
        }
    }

    public void start() {
        start(updateInterval);
    }

    public void start(final long updateInterval) {
        if (!isRunning()) {
            jvm.clear();
            stopped = new CountDownLatch(1);
            this.updateInterval = updateInterval;
            updater = new Thread("MycilaUJD-UpdaterThread") {
                @Override
                public void run() {
                    while (isRunning()
                            && !Thread.currentThread().isInterrupted()) {
                        try {
                            update();
                            Thread.sleep(updateInterval * 1000);
                        } catch (Exception e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    updater = null;
                    stopped.countDown();
                }
            };
            updater.setDaemon(false);
            updater.start();
        } else throw new IllegalStateException("Already started !");
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public boolean isRunning() {
        return updater != null;
    }

    public void await() throws InterruptedException {
        if (isRunning()) stopped.await();
    }
}
