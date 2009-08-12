package com.mycila.cards;

/**
 * @author Mathieu Carbou
 */
public interface HandFilter {
    boolean accept(Hand hand);
}
