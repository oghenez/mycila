package com.mycila.event.api;

import com.mycila.event.api.ErrorHandler;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ErrorHandlerProvider {
    ErrorHandler get();
}
