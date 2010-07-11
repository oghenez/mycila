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

package com.mycila.plugin.scope.defaults;

import com.mycila.plugin.scope.ScopeProviderSkeleton;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Same as {@link Singleton}, except that the result is put in a Weak reference
 * so that it can be garbadged collected in case the export is not needed anymore.
 * The method will be called to get another instance if the reference has been garbadged collected.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class WeakSingleton<T> extends ScopeProviderSkeleton<T> {
    private volatile Reference<T> instance = new WeakReference<T>(null);

    @Override
    public T get() {
        T t = instance.get();
        if (t == null) {
            synchronized (this) {
                t = instance.get();
                if (t == null) {
                    t = (T) context.getInvokable().invoke();
                    instance = new WeakReference<T>(t);
                }
            }
        }
        return t;
    }
}
