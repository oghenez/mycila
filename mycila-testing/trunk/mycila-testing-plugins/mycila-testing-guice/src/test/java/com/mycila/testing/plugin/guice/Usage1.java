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
package com.mycila.testing.plugin.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.mycila.testing.core.TestSetup;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@GuiceContext(AModule.class)
public final class Usage1 {

    @Inject
    @Named("service1")
    Service service1;

    @Inject
    Service service2;

    @Bind
    Provider<Service> service2Provider = new Provider<Service>() {
        public Service get() {
            return new Service() {
                public String go() {
                    return "go2";
                }
            };
        }
    };

    @Bind
    String parameter1 = "parameter";

    @Inject
    String injectedParameter1;

    

    @BeforeClass
    public void setup() {
        TestSetup.setup(this);
    }

    @Test
    public void test_inject() {

    }
}
