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

package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.Loader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class JavaClassImpl<T> implements JavaClass<T> {

    protected final Class<T> theClass;
    protected final DefaultJVM jvm;

    JavaClassImpl(DefaultJVM jvm, Class<T> theClass) {
        this.jvm = jvm;
        this.theClass = theClass;
    }

    public final Loader getLoader() {
        return jvm.loaderRegistry.get(this);
    }

    public final Class<T> get() {
        return theClass;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaClassImpl that = (JavaClassImpl) o;
        return theClass.equals(that.theClass) && getLoader().equals(that.getLoader());
    }

    @Override
    public final int hashCode() {
        return 31 * theClass.hashCode() + theClass.getClassLoader().hashCode();
    }

    @Override
    public String toString() {
        return theClass.getName();
    }
}
