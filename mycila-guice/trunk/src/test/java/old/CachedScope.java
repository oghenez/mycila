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

package old;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class CachedScope implements Scope {

    private final Map<Key<?>, Object> cache = new ConcurrentHashMap<Key<?>, Object>();
    private final ExpirationStrategy expirationStrategy;
    private final ReloadStrategy reloadStrategy;

    CachedScope(ExpirationStrategy expirationStrategy, ReloadStrategy reloadStrategy) {
        this.expirationStrategy = expirationStrategy;
        this.reloadStrategy = reloadStrategy;
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
        return new Provider<T>() {
            @Override
            public T get() {
                T cachedValue = (T) cache.get(key);
                if (cachedValue == null)
                    cache.put(key, cachedValue = (T) reloadStrategy.load(key, creator));
                else if(expirationStrategy.hasExpired(cachedValue))
                    cache.put(key, cachedValue = (T) reloadStrategy.reload(key, creator, cachedValue));
                return cachedValue;
            }
        };
    }
}