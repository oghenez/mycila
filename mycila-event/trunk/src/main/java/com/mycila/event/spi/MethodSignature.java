/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.event.spi;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class MethodSignature {
    private final int hash;
    private final String desc;

    private MethodSignature(Method method) {
        int h = method.getName().hashCode();
        h = h * 31 + method.getParameterTypes().length;
        Class[] params = method.getParameterTypes();
        for (Class parameterType : params)
            h = h * 31 + parameterType.hashCode();
        this.hash = h;
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName()).append("(");
        for (int j = 0, max = params.length - 1; j <= max; j++) {
            sb.append(getTypeName(params[j]));
            if (j < max)
                sb.append(",");
        }
        sb.append(")");
        this.desc = sb.toString();
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public String toString() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MethodSignature && desc.equals(((MethodSignature) o).desc);
    }

    static MethodSignature of(Method m) {
        return new MethodSignature(m);
    }

    private static String getTypeName(Class type) {
        StringBuilder sb = new StringBuilder();
        while (type.isArray()) {
            sb.append("[]");
            type = type.getComponentType();
        }
        sb.insert(0, type.getName());
        return sb.toString();
    }

}
