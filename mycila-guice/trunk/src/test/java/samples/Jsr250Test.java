/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
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

package samples;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Stage;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;
import com.mycila.inject.jsr250.Jsr250Module;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Named;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class Jsr250Test {
    @Test
    public void test() throws Exception {
        Jsr250Injector jsr250Injector = Jsr250.createInjector(Stage.PRODUCTION, new MyModule());
        Injector injector = Guice.createInjector(Stage.PRODUCTION, new MyModule(), new Jsr250Module());

        assertEquals("234", injector.getInstance(Account.class).getNumber());
    }

    static final class MyModule extends AbstractModule {
        @Override
        protected void configure() {
        }

        @Provides
        @Named("RNG")
        Id rng() {
            return new Id(4);
        }
    }

}