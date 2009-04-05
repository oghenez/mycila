/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.testing.core;

/**
 * Holds the current test context. The context holder contains the current test instance used for each call to
 * plugin methods for preparing instance, before and after test execution.<p/>
 * <p/>
 * You can safely use in your plugin this class to get from anywhere the current test {@link com.mycila.testing.core.Context}
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ContextHolder {

    private static final ThreadLocal<Context> ctx = new InheritableThreadLocal<Context>();

    private ContextHolder() {
    }

    static void set(Context c) {
        ctx.set(c);
    }

    static void unset() {
        ctx.remove();
    }

    /**
     * @return The test context
     */
    public static Context get() {
        Context c = ctx.get();
        if (c == null) {
            throw new IllegalStateException("There is not Context bound to local thread !");
        }
        return c;
    }
}
