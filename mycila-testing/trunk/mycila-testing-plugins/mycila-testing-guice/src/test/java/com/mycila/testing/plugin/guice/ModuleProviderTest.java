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

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.mycila.testing.core.MycilaTesting;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ModuleProviderTest {

    @Inject
    Injector injector;

    @Test
    public void test_setup() {
        MycilaTesting.from(getClass()).handle(this).prepare();
        assertNotNull(injector.getBinding(Key.get(String.class)));
        assertNotNull(injector.getBinding(Key.get(int.class)));
        assertNotNull(injector.getBinding(Key.get(float.class)));
        assertNotNull(injector.getBinding(Key.get(File.class)));
        assertNotNull(injector.getBinding(Key.get(Service.class)));
    }

    @ModuleProvider
    private Module provides1() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class).toInstance("do what i say but not what i do");
            }
        };
    }

    @ModuleProvider
    Module[] provides2() {
        return new Module[]{new AbstractModule() {
            @Override
            protected void configure() {
                bind(int.class).toInstance(0);
            }
        }, new AbstractModule() {
            @Override
            protected void configure() {
                bind(float.class).toInstance(0.0f);
            }
        }};
    }

    @ModuleProvider
    public Iterable<Module> provides3() {
        return new ArrayList<Module>() {
            {
                add(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(File.class).toInstance(new File("."));
                    }
                });
                add(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(Service.class).to(ServiceImpl.class);
                    }
                });
            }

            private static final long serialVersionUID = -5404479570043533205L;
        };
    }

}