package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AnalyzerException extends RuntimeException {
    AnalyzerException(String message) {
        super(message);
    }
}
