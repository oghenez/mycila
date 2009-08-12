package com.mycila.cards;

import com.mycila.distribution.Distribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou
 */
public final class Hand implements Iterable<Card> {

    private final SortedSet<Card> cards = new TreeSet<Card>();
    private final Game game;

    Hand(Game game) {
        this.game = game;
    }

    public Game game() {
        return game;
    }

    public Hand add(Hand... hands) {
        for (Hand hand : hands) add(hand.cards);
        return this;
    }

    public Hand add(Card... cards) {
        return add(Arrays.asList(cards));
    }

    public Hand add(Collection<Card> cards) {
        this.cards.addAll(cards);
        return this;
    }

    public Hand remove(Hand... hands) {
        for (Hand hand : hands) remove(hand.cards);
        return this;
    }

    public Hand remove(Card... cards) {
        return remove(Arrays.asList(cards));
    }

    public Hand remove(Collection<Card> cards) {
        this.cards.removeAll(cards);
        return this;
    }

    public Card getHigherCard() {
        return isEmpty() ? null : cards.last();
    }

    public Distribution<CardValue> cardValueMap() {
        Distribution<CardValue> distribution = Distribution.of(CardValue.class);
        for (Card card : this) distribution.add(card.value());
        return distribution;
    }

    public Distribution<CardSuit> cardSuitMap() {
        Distribution<CardSuit> distribution = Distribution.of(CardSuit.class);
        for (Card card : this) distribution.add(card.suit());
        return distribution;
    }

    public HandRank rank() {
        return game.handRankFactory(this).createHandRank(this);
    }

    public Iterator<Card> reverseIterator() {
        SortedSet<Card> reversed = new TreeSet<Card>(Collections.reverseOrder());
        reversed.addAll(cards);
        return Collections.unmodifiableSortedSet(reversed).iterator();
    }

    @Override
    public Iterator<Card> iterator() {
        return Collections.unmodifiableSortedSet(cards).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hand hand = (Hand) o;
        return cards.equals(hand.cards);
    }

    @Override
    public int hashCode() {
        return cards.hashCode();
    }

    @Override
    public String toString() {
        return cards.toString();
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Hand empty() {
        cards.clear();
        return this;
    }

    public SortedSet<Card> cards() {
        return Collections.unmodifiableSortedSet(cards);
    }

    public List<Card> asList() {
        return Collections.unmodifiableList(new ArrayList<Card>(cards));
    }

    public Hand duplicate() {
        return new Hand(game).add(cards);
    }

    public Hand extract(CardValue... cardValues) {
        return extract(Arrays.asList(cardValues));
    }

    public Hand extract(Collection<CardValue> cardValues) {
        Hand hand = game().handEmpty();
        for (CardValue cardValue : cardValues) {
            for (Card card : this) {
                if (card.value() == cardValue) {
                    hand.add(card);
                }
            }
        }
        return hand;
    }

}
