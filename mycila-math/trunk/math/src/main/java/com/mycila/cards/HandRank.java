package com.mycila.cards;

import java.util.List;

/**
 * @author Mathieu Carbou
 */
public interface HandRank extends Comparable<HandRank> {
    String describe();

    Hand currentHand();

    List<Card> rankedHand();
}
