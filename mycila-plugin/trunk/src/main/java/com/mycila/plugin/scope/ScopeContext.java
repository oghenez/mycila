package com.mycila.plugin.scope;

import com.mycila.plugin.Invokable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ScopeContext<T> {

    /**
     * Get the invokable returning the instance of T 
     *
     * @return The service
     */
    Invokable getInvokable();

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
