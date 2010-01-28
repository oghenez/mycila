package com.mycila.ujd.mbean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JmxUpdaterMBean {
    long getIntervalInSeconds();

    void start();

    void startWithUpdateIntervalInSeconds(long seconds);

    void updateNow();

    void stop();

    boolean isRunning();
}