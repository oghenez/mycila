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

package com.mycila.inject.injector;

import java.util.Arrays;

final class MethodKey {
    private final String name;
    private final Class[] parameterTypes;
    private final int hashcode;

    MethodKey(String name, Class... parameterTypes) {
        this.name = name;
        this.parameterTypes = parameterTypes;
        int result = name.hashCode();
        result = 31 * result + Arrays.hashCode(parameterTypes);
        this.hashcode = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodKey methodKey = (MethodKey) o;
        return name.equals(methodKey.name) && Arrays.equals(parameterTypes, methodKey.parameterTypes);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }
}
