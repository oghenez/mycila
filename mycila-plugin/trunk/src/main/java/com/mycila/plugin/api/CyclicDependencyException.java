package com.mycila.plugin.api;

import static java.lang.String.*;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CyclicDependencyException extends PluginException {
    private static final long serialVersionUID = -6644476561325060279L;

    private final SortedMap<String, Plugin> cyclics;

    public CyclicDependencyException(SortedMap<String, Plugin> cyclics) {
        super(format("Cyclic dependencies have been found amongst these plugins:\n%s", info(cyclics)));
        this.cyclics = Collections.unmodifiableSortedMap(cyclics);
    }

    public SortedMap<String, Plugin> getCyclics() {
        return cyclics;
    }

    private static String info(SortedMap<String, Plugin> cyclics) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Plugin> entry : cyclics.entrySet()) {
            sb.append("- Plugin '").append(entry.getKey()).append("'").append("\n");
            sb.append("    - befores: ").append(entry.getValue().getBefore()).append("\n");
            sb.append("    - afters: ").append(entry.getValue().getAfter()).append("\n");
        }
        return sb.toString();
    }

}
