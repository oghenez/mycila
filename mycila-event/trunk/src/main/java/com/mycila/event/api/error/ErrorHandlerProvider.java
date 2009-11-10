package com.mycila.event.api.error;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ErrorHandlerProvider {
    ErrorHandler get();
}
