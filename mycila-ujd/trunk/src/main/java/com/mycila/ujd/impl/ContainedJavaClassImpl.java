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

import com.mycila.ujd.api.ContainedJavaClass;
import com.mycila.ujd.api.Container;

import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ContainedJavaClassImpl<T> extends JavaClassImpl<T> implements ContainedJavaClass<T> {

    ContainedJavaClassImpl(DefaultJVM jvm, Class<T> theClass) {
        super(jvm, theClass);
    }

    public Container getContainer() {
        return jvm.containerRegistry.get(this);
    }

    public URL getURL() {
        return getLoader().get().getResource(getPath());
    }

    public String getPath() {
        return ClassUtils.getPath(theClass);
    }

    public String getClassName() {
        return theClass.getName();
    }
}
