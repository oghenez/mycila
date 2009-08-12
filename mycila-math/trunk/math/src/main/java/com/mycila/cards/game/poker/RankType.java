package com.mycila.cards.game.poker;

import com.mycila.cards.Card;
import com.mycila.cards.CardValue;
import com.mycila.cards.Hand;
import com.mycila.cards.HandFilter;
import com.mycila.cards.HandRank;
import com.mycila.cards.HandRankFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mathieu Carbou
 */
enum RankType implements HandRankFactory, HandFilter {

    /* none */
    NONE("Nothing") {
        public boolean accept(Hand hand) {
            return true;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return Collections.emptyList();
                }
            };
        }},

    /* Highest value card */
    HIGH_CARD("Highest card value") {
        public boolean accept(Hand hand) {
            return !hand.isEmpty();
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return Arrays.asList(currentHand().getHigherCard());
                }
            };
        }},

    /* Two cards of the same value */
    ONE_PAIR("One pair") {
        public boolean accept(Hand hand) {
            return hand.cardValueMap().itemsHavingCount(2).size() == 1;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return currentHand().extract(currentHand().cardValueMap().itemsHavingCount(2)).asList();
                }
            };
        }},

    /* Two different pairs */
    TWO_PAIRS("Two pairs") {
        public boolean accept(Hand hand) {
            return hand.cardValueMap().itemsHavingCount(2).size() == 2;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return currentHand().extract(currentHand().cardValueMap().itemsHavingCount(2)).asList();
                }
            };
        }},

    /* Three cards of the same value */
    THREE_OF_A_KIND("Three of a kind") {
        public boolean accept(Hand hand) {
            return hand.cardValueMap().itemsHavingCount(3).size() == 1;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return currentHand().extract(currentHand().cardValueMap().itemsHavingCount(3)).asList();
                }
            };
        }},

    /* All cards are consecutive values */
    STRAIGHT("Straight") {
        public boolean accept(Hand hand) {
            return hasConsecutiveValues(hand);
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return currentHand().asList();
                }
            };
        }},

    /* All cards of the same suit */
    FLUSH("Flush") {
        public boolean accept(Hand hand) {
            return hand.cardSuitMap().itemsHavingCount(hand.size()).size() == 1;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return currentHand().asList();
                }
            };
        }},

    /* Three of a kind and a pair */
    FULL_HOUSE("Full house") {
        public boolean accept(Hand hand) {
            return hand.cardValueMap().itemsHavingCount(2).size() == 1
                    && hand.cardValueMap().itemsHavingCount(3).size() == 1;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    List<Card> res = new ArrayList<Card>();
                    res.addAll(currentHand().extract(currentHand().cardValueMap().itemsHavingCount(2)).asList());
                    res.addAll(currentHand().extract(currentHand().cardValueMap().itemsHavingCount(3)).asList());
                    return res;
                }
            };
        }},

    /* Four cards of the same value */
    FOUR_OF_A_KIND("Four of a kind") {
        public boolean accept(Hand hand) {
            return hand.cardValueMap().itemsHavingCount(4).size() == 1;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return currentHand().extract(currentHand().cardValueMap().itemsHavingCount(4)).asList();
                }
            };
        }},

    /* All cards are consecutive values of same suit */
    STRAIGHT_FLUSH("Straight flush") {
        public boolean accept(Hand hand) {
            return hasConsecutiveValues(hand)
                    && hand.cardSuitMap().itemsHavingCount(hand.size()).size() == 1;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return currentHand().asList();
                }
            };
        }},

    /* Ten, Jack, Queen, King, Ace, in same suit */
    ROYAL_FLUSH("Royal flush") {
        public boolean accept(Hand hand) {
            return hasConsecutiveValues(hand)
                    && hand.cardSuitMap().itemsHavingCount(hand.size()).size() == 1
                    && hand.iterator().next().value() == CardValue.TEN;
        }
        public HandRank createHandRank(Hand hand) {
            return new PokerHandRank(this, hand) {
                @Override
                protected List<Card> rankedHand(Hand newEmptyHand) {
                    return currentHand().asList();
                }
            };
        }};

    private final String name;

    private RankType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static RankType getHigherRank(Hand hand) {
        RankType[] types = values();
        for (int i = types.length - 1; i >= 0; i--) {
            if (types[i].accept(hand)) {
                return types[i];
            }
        }
        throw new IllegalStateException("BUG in the code !");
    }

    private static boolean hasConsecutiveValues(Hand hand) {
        Iterator<Card> it = hand.iterator();
        Card old = it.next();
        while (it.hasNext()) {
            Card current = it.next();
            if (current.value().ordinal() - old.value().ordinal() != 1)
                return false;
            old = current;
        }
        return true;
    }

}
