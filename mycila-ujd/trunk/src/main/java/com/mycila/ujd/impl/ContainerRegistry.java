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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ContainerRegistry {

    private final ConcurrentHashMap<URL, Container> containers = new ConcurrentHashMap<URL, Container>();

    Container get(URL url) {
        Container container = containers.get(url);
        if (container == null)
            containers.put(url, container = new ContainerImpl(url));
        return container;
    }

    Container get(ContainedClass theClass) {
        final String resource = theClass.getPath();
        final URL resUrl = theClass.getURL();
        final StringBuilder url = new StringBuilder(resUrl.toExternalForm());
        try {
            if ("jar".equals(resUrl.getProtocol()))
                url.delete(0, 4).delete(url.indexOf(resource) - 2, url.length());
            else if ("file".equals(resUrl.getProtocol()))
                url.delete(url.indexOf(resource), url.length());
            else
                throw new IllegalStateException("Cannot get container for resource [" + resource + "]. Unsupported URL protocol [" + resUrl.getProtocol() + "].");
            return get(new URL(url.toString()));
        }
        catch (MalformedURLException e) {
            throw new IllegalStateException("Cannot get container for resource [" + resource + "]. Malformed URL [" + url + "].", e);
        }
    }

    public void clear() {
        containers.clear();
    }
}