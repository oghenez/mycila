package com.mycila.log;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface LoggerProvider {
    Logger get(String name);
}
