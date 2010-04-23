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

package com.mycila.jmx.export;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO
public class AnnotationMetadataAssembler extends ReflectionMetadataAssemblerSkeleton {

    @Override
    public boolean canInclude(Class<?> managedClass, Field field) {
        return false;
    }

    @Override
    public boolean canInclude(Class<?> managedClass, BeanProperty property) {
        return false;
    }

    @Override
    public boolean canInclude(Class<?> managedClass, Method method) {
        return false;
    }

}