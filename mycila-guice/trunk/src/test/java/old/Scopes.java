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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Scopes {

    private Scopes() {
    }

    public static Scope cachedScope(long duration, TimeUnit unit) {
        final long delay = unit.toNanos(duration);
        final ConcurrentHashMap<Object, Long> times = new ConcurrentHashMap<Object, Long>();
        return new CachedScope(new ExpirationStrategy() {
            @Override
            public boolean hasExpired(Object val) {
                Long time = times.get(val);
                return time == null || System.nanoTime() - time >= delay;
            }
        }, new ReloadStrategy() {
            @Override
            public Object load(Key key, Provider creator) {
                final Object t = creator.get();
                times.put(t, System.nanoTime());
                return t;
            }

            @Override
            public Object reload(Key key, Provider creator, Object current) {
                times.remove(current);
                final Object t = creator.get();
                times.put(t, System.nanoTime());
                return t;
            }
        });
    }

    public static Scope cachedScope(ExpirationStrategy expirationStrategy, ReloadStrategy reloadStrategy) {
        return new CachedScope(expirationStrategy, reloadStrategy);
    }

}