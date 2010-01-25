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
        return className != null ? className.hashCode() : 0;
    }

    @Override
    public String toString() {
        return className;
    }
}
