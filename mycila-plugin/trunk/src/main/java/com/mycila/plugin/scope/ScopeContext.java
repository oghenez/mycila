package com.mycila.plugin.scope;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ScopeContext<T> {

    /**
     * Invoke the exporting method
     *
     * @return The service
     */
    T invoke();

    /**
     * Check if a scope parameter exists
     *
     * @param name Scope parameter name
     * @return true if exist
     */
    boolean hasParameter(String name);

    /**
     * Get a scope parameter
     *
     * @param name Scope parameter name
     * @return the parameter value
     * @throws MissingScopeParameterException If no parameter exist with this name
     */
    String getParameter(String name) throws MissingScopeParameterException;

    String toString();
}
