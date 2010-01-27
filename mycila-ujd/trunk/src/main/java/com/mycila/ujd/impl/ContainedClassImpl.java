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

import com.mycila.ujd.api.ContainedClass;
import com.mycila.ujd.api.Container;

import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ContainedClassImpl implements ContainedClass {

    private final ContainerImpl container;
    private final String path;
    private final String className;

    ContainedClassImpl(ContainerImpl container, String path) {
        this.container = container;
        this.path = path.startsWith("/") ? path.substring(1) : path;
        final String cname = this.path.replace('/', '.').replace('\\', '.');
        this.className = cname.substring(0, cname.length() - 6);
    }

    public Container getContainer() {
        return container;
    }

    public String getClassName() {
        return className;
    }

    public String getPath() {
        return path;
    }

    public URL getURL() {
        return container.getURL(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainedClassImpl that = (ContainedClassImpl) o;
        return !(className != null ? !className.equals(that.className) : that.className != null);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    @Override
    public String toString() {
        return className;
    }
}
