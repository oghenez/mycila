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

package com.mycila.plugin.spi.model;

import com.mycila.plugin.Provider;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ProviderAdvisor<T> implements Provider<T> {

    private final PluginExport<T> export;
    private Provider<T> chain;

    private ProviderAdvisor(PluginExport<T> export) {
        this.export = export;
        this.chain = new ChainedProvider(null, null);
    }

    public ProviderAdvisor<T> addInterceptor(ProviderInterceptor interceptor) {
        chain = new ChainedProvider(interceptor, chain);
        return this;
    }

    @Override
    public T get() {
        return chain.get();
    }

    public static <T> ProviderAdvisor<T> create(PluginExport<T> export) {
        return new ProviderAdvisor<T>(export);
    }

    private final class ChainedProvider implements Provider<T> {
        private final ProviderInterceptor interceptor;
        private final Provider<T> next;

        private ChainedProvider(ProviderInterceptor interceptor, Provider<T> next) {
            this.interceptor = interceptor;
            this.next = next;
        }

        @Override
        public T get() {
            return next == null ? export.getProvider().get() : interceptor.get(export, next);
        }
    }
}
