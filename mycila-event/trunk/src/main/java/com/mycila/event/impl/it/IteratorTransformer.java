package com.mycila.event.impl.it;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface IteratorTransformer<S, D> {
    D transform(S source);
}