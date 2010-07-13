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

import com.mycila.plugin.scope.ScopeContext;
import com.mycila.plugin.scope.ScopeProviderSkeleton;

/**
 * Same as {@link Singleton}, except that the result is only valid for N milliseconds.
 * The time is specified by the parameter 'duration', in milliseconds.
 * When the export is requested and the duration expired, the method will be called to refresh the instance.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExpiringSingleton extends ScopeProviderSkeleton {

    private long duration;

    private volatile Object instance;
    private volatile long expire;

    @Override
    public void init(ScopeContext context) {
        super.init(context);
        this.duration = Long.parseLong(context.getParameter("duration"));
    }

    @Override
    public Object get() {
        if (expire < System.currentTimeMillis()) {
            synchronized (this) {
                if (expire < System.currentTimeMillis()) {
                    expire = System.currentTimeMillis() + duration;
                    instance = context.getInvokable().invoke();
                }
            }
        }
        return instance;
    }
}
