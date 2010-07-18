/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.plugin.spi.internal.aop;

import com.mycila.plugin.Provider;
import com.mycila.plugin.err.InactivePluginException;
import com.mycila.plugin.err.InjectionInProgressException;
import com.mycila.plugin.spi.internal.model.Binding;
import com.mycila.plugin.spi.internal.model.PluginExport;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ProviderProxy {

    private final Binding<?> binding;
    private final PluginExport<?> export;

    public ProviderProxy(Binding<?> binding, PluginExport<?> export) {
        this.binding = binding;
        this.export = export;
    }

    public static ProviderProxy create(Binding<?> binding, PluginExport<?> export) {
        return new ProviderProxy(binding, export);
    }

    public Object build() {
        Provider<?> provider = ExportReadyProvider.ensureExportReady(export);
        if (binding.isProvided()) return provider;
        Class<?> type = binding.getType().getRawType();
        /*if (type.isInterface())
            return ProxyTOREFAC.createJDKProxy();*/
        //TODO
        return null;
    }

    private static final class ExportReadyProvider<T> implements Provider<T> {
        private final PluginExport<T> export;

        private ExportReadyProvider(PluginExport<T> export) {
            this.export = export;
        }

        @Override
        public T get() {
            if (!export.getPluginMetadata().isResolved())
                throw new InjectionInProgressException(export.getPluginMetadata());
            if (!export.getPluginMetadata().isStarted())
                throw new InactivePluginException(export);
            return export.getProvider().get();
        }

        public static <T> Provider<T> ensureExportReady(PluginExport<T> export) {
            return new ExportReadyProvider<T>(export);
        }
    }

}
