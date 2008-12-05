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

import com.google.inject.Module;
import com.google.inject.Stage;

import java.lang.annotation.*;

/**
 * The GuiceContext annotation can be placed on a Test Class to specify
 * a list of class modules to load and a stage to use for the Injector.
 * The default stage is DEVELOPMENT.
 * <p>
 * If some modules you want to use requires some parameters to be instanciated,
 * you can use the {@link com.mycila.testing.plugin.guice.ModuleProvider} annotation
 * on a method to provide directly Module instances.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface GuiceContext {
    Class<? extends Module>[] value() default {};
    Stage stage() default Stage.DEVELOPMENT;    
}
