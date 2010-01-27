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
