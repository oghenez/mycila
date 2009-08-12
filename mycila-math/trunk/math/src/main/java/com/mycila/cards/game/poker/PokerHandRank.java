package com.mycila.cards.game.poker;

import com.mycila.cards.Card;
import com.mycila.cards.Hand;
import com.mycila.cards.HandRank;

import java.util.Iterator;
import java.util.List;

/**
 * @author Mathieu Carbou
 */
abstract class PokerHandRank implements HandRank {

    private final RankType rankType;
    private final Hand hand;
    private final List<Card> rankedHand;

    PokerHandRank(RankType rankType, Hand hand) {
        this.rankType = rankType;
        this.hand = hand;
        this.rankedHand = rankedHand(hand.game().handEmpty());
    }

    public final RankType rankType() {
        return rankType;
    }

    @Override
    public final Hand currentHand() {
        return hand;
    }

    @Override
    public final int compareTo(HandRank other) {
        PokerHandRank o = (PokerHandRank) other;
        // check rank type
        int compare = rankType.compareTo(o.rankType);
        if (compare != 0) return compare;
        // check card values for rank
        for (int i = rankedHand().size() - 1; i >= 0; i--) {
            compare = rankedHand().get(i).value().compareTo(other.rankedHand().get(i).value());
            if (compare != 0) return compare;
        }
        // then check higher card
        for (Iterator<Card> thisIt = currentHand().duplicate().remove(rankedHand()).reverseIterator(),
                otherIt = o.currentHand().duplicate().remove(o.rankedHand()).reverseIterator(); thisIt.hasNext() && otherIt.hasNext();) {
            compare = thisIt.next().value().compareTo(otherIt.next().value());
            if (compare != 0) return compare;
        }
        return 0;
    }

    @Override
    public final String toString() {
        return describe();
    }

    @Override
    public final String describe() {
        return rankType().toString() + " " + rankedHand();
    }

    @Override
    public List<Card> rankedHand() {
        return rankedHand;
    }

    protected abstract List<Card> rankedHand(Hand newEmptyHand);

}
