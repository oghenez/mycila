package com.mycila.plugin.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CyclicDependencyException extends PluginException {
    private static final long serialVersionUID = -6644476561325060279L;

    public CyclicDependencyException() {
        super("");
    }
}
