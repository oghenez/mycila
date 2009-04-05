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

import java.lang.reflect.Method;
import java.util.List;

/**
 * Adapter implementation of a Test Plugin which does nothing.
 * <strong>It is strongly adviced that plugins extends this class
 * instead of implementing directly {@link com.mycila.testing.core.TestPlugin} interfaces</strong>,
 * to avoid any code break when changing or enhancing the API.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class DefaultTestPlugin implements TestPlugin {
    public void prepareTestInstance(Context context) throws Exception {
    }

    public List<String> getBefore() {
        return null;
    }

    public List<String> getAfter() {
        return null;
    }

    public boolean beforeTest(Context context, Method method) throws Exception {
        return true;
    }

    public void afterTest(Context context, Method method, Throwable throwable) throws Exception {
    }

    public void afterClass(Context context) throws Exception {
    }
}
