package com.mycila.testing.plugin.spring;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MyBean {
    public final String txt;

    public MyBean(String s) {
        this.txt = s;
    }

    @Override
    public String toString() {
        return txt;
    }
}
