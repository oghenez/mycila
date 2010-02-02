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

package com.mycila.ujd.mbean;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.mycila.ujd.api.JVMAnalyzer;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JmxAnalyzer implements JmxAnalyzerMBean {

    private final JVMAnalyzer analyzer;
    private boolean html;

    public JmxAnalyzer(JVMAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void setHTMLOutput(boolean b) {
        html = b;
    }

    public boolean isHTMLOutput() {
        return html;
    }

    public int getClassCount() {
        return analyzer.getClassCount();
    }

    public int getLoaderCount() {
        return analyzer.getLoaderCount();
    }

    public String getLoaderNames() {
        return asString(sort(analyzer.getLoaderNames()));
    }

    public String getLoaderNames(String packagePrefix) {
        return asString(sort(analyzer.getLoaderNames(packagePrefix)));
    }

    public String getClasses(String loaderName, String packagePrefix) {
        return asString(sort(analyzer.getClasses(loaderName, packagePrefix)));
    }

    public String getUsedClasses(String loaderName, String packagePrefix) {
        return asString(sort(analyzer.getUsedClasses(loaderName, packagePrefix)));
    }

    public String getUnusedClasses(String loaderName, String packagePrefix) {
        return asString(sort(analyzer.getUnusedClasses(loaderName, packagePrefix)));
    }

    public String getClassPath(String loaderName) {
        return asString(analyzer.getClassPath(loaderName));
    }

    public String getUsedClassPath(String loaderName) {
        return asString(sort(analyzer.getUsedClassPath(loaderName)));
    }

    public String getUnusedClassPath(String loaderName) {
        return asString(sort(analyzer.getUnusedClassPath(loaderName)));
    }

    public String getContainers(String packagePrefix) {
        return asString(sort(analyzer.getContainers(packagePrefix)));
    }

    public String getUsedContainers(String packagePrefix) {
        return asString(sort(analyzer.getUsedContainers(packagePrefix)));
    }

    private <T> Iterable<String> sort(Iterable<T> it) {
        Set<String> sorted = new TreeSet<String>();
        Iterables.addAll(sorted, Iterables.transform(it, Functions.toStringFunction()));
        return sorted;
    }

    private <T> String asString(Iterable<T> it) {
        StringBuilder sb = new StringBuilder();
        for (T t : it) sb.append(t).append("\n");
        return isHTMLOutput() ? sb.toString().replaceAll("\n", "<br/>") : sb.toString();
    }
}
