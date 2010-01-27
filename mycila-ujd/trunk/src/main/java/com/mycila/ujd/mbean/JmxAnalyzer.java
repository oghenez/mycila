package com.mycila.ujd.mbean;

import com.mycila.ujd.api.Analyzer;
import com.mycila.ujd.impl.MycilaUJDAnalyzer;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JmxAnalyzer implements JmxAnalyzerMBean {

    private final Analyzer analyzer;

    public JmxAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void close() {
        analyzer.close();
    }
}
