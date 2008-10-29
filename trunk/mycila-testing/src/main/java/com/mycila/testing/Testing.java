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

package com.mycila.testing;

import com.mycila.testing.core.DefaultTestManager;
import com.mycila.testing.core.TestManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Testing {

    private static final Testing instance = new Testing();

    private final TestManager manager;

    public Testing() {
        
        manager = new DefaultTestManager();
    }

    public static void excludeLoading(String... plugins) {
        
    }

    public static void prepare(Object testInstance) {
        instance.manager.prepare(testInstance);
    }

}
