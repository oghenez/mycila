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

package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JVMAnalyzer {
    JVM getJVM();

    int getClassCount();

    int getLoaderCount();

    Iterable<String> getLoaderNames();

    Iterable<String> getLoaderNames(String packagePrefix);

    Iterable<? extends Container> getClassPath(String loaderName);

    Iterable<? extends Container> getUsedClassPath(String loaderName);

    Iterable<? extends Container> getUnusedClassPath(String loaderName);

    Iterable<? extends ContainedClass> getClasses(String loaderName, String packagePrefix);

    Iterable<? extends ContainedJavaClass<?>> getUsedClasses(String loaderName, String packagePrefix);

    Iterable<? extends ContainedClass> getUnusedClasses(String loaderName, String packagePrefix);

    Iterable<? extends Container> getUsedContainers(String packagePrefix);

    Iterable<? extends Container> getContainers(String packagePrefix);
}
