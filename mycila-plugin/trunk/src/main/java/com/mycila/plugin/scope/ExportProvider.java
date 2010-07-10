package com.mycila.plugin.scope;

import com.mycila.plugin.Provider;

/**
 * Implemented by scopes to provide an export.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ExportProvider<T> extends Provider<T> {

    /**
     * @param context The scope context, form which you can invoke the exporting method and get scope parameters
     */
    void init(ScopeContext<T> context);
}