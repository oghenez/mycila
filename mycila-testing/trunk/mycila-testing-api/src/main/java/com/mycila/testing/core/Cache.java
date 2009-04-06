package com.mycila.testing.core;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum Cache {
    /**
     * The plugin cache will be statically shared bewteen all test instances
     */
    SHARED,

    /**
     * The plugin cache will be recreated for each test instance, thus allowing to control the cache more specifically for a test
     */
    UNSHARED
}
