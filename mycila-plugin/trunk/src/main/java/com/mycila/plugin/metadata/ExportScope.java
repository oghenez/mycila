package com.mycila.plugin.metadata;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ExportScope {
    boolean hasParameter(String name);
    String getParameter(String name);
    String toString();
}
