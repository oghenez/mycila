package com.mycila.plugin.metadata.model;

import com.mycila.plugin.metadata.ExportScope;
import com.mycila.plugin.scope.MissingScopeParameterException;
import com.mycila.plugin.scope.ScopeProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExportScopeImpl<T> implements ExportScope {

    private final Map<String, String> params = new HashMap<String, String>();
    private final Class<? extends ScopeProvider<T>> scope;

    public ExportScopeImpl(Class<? extends ScopeProvider<T>> scope) {
        this.scope = scope;
    }

    public ExportScopeImpl(Class<? extends ScopeProvider<T>> scope, Map<String, String> params) {
        this.scope = scope;
        this.params.putAll(params);
    }

    @Override
    public boolean hasParameter(String name) {
        return params.containsKey(name);
    }
    
    @Override
    public String getParameter(String name) {
        String val = params.get(name);
        if (val == null)
            throw new MissingScopeParameterException(null, "duration");
        return val;
    }
}
