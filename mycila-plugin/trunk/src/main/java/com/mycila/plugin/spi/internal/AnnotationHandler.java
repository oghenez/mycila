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

package com.mycila.plugin.spi.internal;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class AnnotationHandler implements InvocationHandler, Serializable {
    final AnnotationMetadata metadata;

    public AnnotationHandler(AnnotationMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String member = method.getName();
        Class[] paramTypes = method.getParameterTypes();
        // Handle Object and Annotation methods
        if (member.equals("equals") && paramTypes.length == 1 && paramTypes[0] == Object.class)
            return metadata.isSameAnnotation(args[0]);
        if (member.equals("toString"))
            return metadata.toString();
        if (member.equals("hashCode"))
            return metadata.hashCode();
        if (member.equals("annotationType"))
            return metadata.getType();
        // Handle annotation member accessors
        return metadata.get(member);
    }

}
