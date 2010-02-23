package com.mycila.jmx.export.annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum Element {

    /**
     * Expose only annotated methods and fields
     */
    ANNOTATED,

    /**
     * Expose ALL methods, even private ones
     */
    ALL,

    /**
     * Expose only public methods
     */
    PUBLIC
}
