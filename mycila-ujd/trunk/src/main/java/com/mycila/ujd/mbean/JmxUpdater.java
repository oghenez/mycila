package com.mycila.ujd.mbean;

import com.mycila.ujd.api.JVMUpdater;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JmxUpdater implements JmxUpdaterMBean {

    private final JVMUpdater jvmUpdater;

    public JmxUpdater(JVMUpdater jvmUpdater) {
        this.jvmUpdater = jvmUpdater;
    }

    public long getIntervalInSeconds() {
        return jvmUpdater.getUpdateInterval();
    }

    public void start() {
        jvmUpdater.start();
    }

    public void startWithUpdateIntervalInSeconds(long seconds) {
        jvmUpdater.start(seconds);
    }

    public void updateNow() {
        jvmUpdater.update();
    }

    public void stop() {
        jvmUpdater.stop();
    }

    public boolean isRunning() {
        return jvmUpdater.isRunning();
    }
}
