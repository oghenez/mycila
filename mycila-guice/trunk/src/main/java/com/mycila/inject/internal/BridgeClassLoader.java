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

package com.mycila.inject.internal;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class BridgeClassLoader extends ClassLoader {
    private final String[] packages;
    private final ClassLoader delegate;

    public BridgeClassLoader(ClassLoader parent, ClassLoader delegate, String... packages) {
        super(parent);
        this.packages = packages;
        this.delegate = delegate;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        for (String p : packages) {
            if (name.startsWith(p)) {
                try {
                    Class<?> clazz = delegate.loadClass(name);
                    if (resolve)
                        resolveClass(clazz);
                    return clazz;
                } catch (Exception e) {
                    // fall back to classic delegation
                }
                return super.loadClass(name, resolve);
            }
        }
        return super.loadClass(name, resolve);
    }
}