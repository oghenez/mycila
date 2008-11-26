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

package com.mycila.testing.junit;

import static com.mycila.testing.core.TestSetup.*;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaJunitRunner extends BlockJUnit4ClassRunner {
    public MycilaJunitRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        setup(test);
        return test;
    }
}
