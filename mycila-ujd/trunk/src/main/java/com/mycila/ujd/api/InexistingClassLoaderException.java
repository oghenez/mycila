package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InexistingClassLoaderException extends AnalyzerException {
    public InexistingClassLoaderException(String clname) {
        super(clname);
    }
}