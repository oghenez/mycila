package com.mycila.testing.plugin.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AutowiredBean {

    @Autowired
    MyBean mybean;

    @Autowired
    @Qualifier("bbean")
    String bbean;

}
