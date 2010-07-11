package com.mycila.plugin.metadata.model;

import com.mycila.plugin.Invokable;
import com.mycila.plugin.Provider;
import com.mycila.plugin.metadata.PluginExport;
import com.mycila.plugin.metadata.PluginMetadata;
import com.mycila.plugin.scope.MissingScopeParameterException;
import com.mycila.plugin.scope.ScopeContext;
import com.mycila.plugin.scope.ScopeProvider;
import com.mycila.plugin.scope.ScopeProviders;
import net.sf.cglib.reflect.FastMethod;

import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginExportMethod<T> implements PluginExport<T> {

    private final FastMethod method;
    private final PluginMetadata plugin;
    private final Class<? extends ScopeProvider> scopeClass;
    private final Provider<? extends T> provider;

    public PluginExportMethod(final PluginMetadata metadata, final FastMethod method, final Class<? extends ScopeProvider> scopeClass, final Map<String, String> parameters) {
        this.plugin = metadata;
        this.method = method;
        this.scopeClass = scopeClass;
        this.provider = ScopeProviders.build(scopeClass, new ScopeContext<T>() {
            final Invokable exportGetter = new InvokableMethod(metadata.getTarget(), method);

            @Override
            public Invokable getInvokable() {
                return exportGetter;
            }

            @Override
            public boolean hasParameter(String name) {
                return parameters.containsKey(name);
            }

            @Override
            public String getParameter(String name) throws MissingScopeParameterException {
                String val = parameters.get(name);
                if (val == null)
                    throw new MissingScopeParameterException(method.getJavaMethod(), scopeClass, name);
                return val;
            }

            @Override
            public String toString() {
                return getType().getName();
            }
        });
    }

    @Override
    public Class<T> getType() {
        return method.getReturnType();
    }

    @Override
    public Class<?> getScope() {
        return scopeClass;
    }

    @Override
    public PluginMetadata getPluginMetadata() {
        return plugin;
    }

    @Override
    public Provider<? extends T> getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return "Export " + getType().getName() + " with scope " + getScope().getSimpleName();
    }
}
