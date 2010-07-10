package com.mycila.plugin.metadata;

import com.mycila.plugin.scope.ExportProvider;
import com.mycila.plugin.scope.MissingScopeParameterException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ScopeMetadata<T> {

    private final Map<String, String> params = new HashMap<String, String>();
    private final Class<? extends ExportProvider<T>> scope;

    public ScopeMetadata(Class<? extends ExportProvider<T>> scope) {
        this.scope = scope;
    }

    public ScopeMetadata(Class<? extends ExportProvider<T>> scope, Map<String, String> params) {
        this.scope = scope;
        this.params.putAll(params);
    }

    public boolean hasParameter(String name) {
        return params.containsKey(name);
    }
    
    public String getParameter(String name) {
        String val = params.get(name);
        if (val == null)
            throw new MissingScopeParameterException(null, "duration");
        return val;
    }
}
