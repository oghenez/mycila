package com.mycila.plugin.discovery;

import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ClassResolver {
    Class<?> resolve(URL url) throws ClassResolverException;
}
