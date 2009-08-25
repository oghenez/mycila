/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package cards;

/**
 * @author Mathieu Carbou
 */
public final class Card implements Comparable<Card> {

    private final CardSuit suit;
    private final CardValue value;

    private Card(CardValue value, CardSuit suit) {
        this.suit = suit;
        this.value = value;
    }

    public CardSuit suit() {
        return suit;
    }

    public CardValue value() {
        return value;
    }

    @Override
    public int compareTo(Card o) {
        int compare = value().compareTo(o.value());
        return compare != 0 ? compare : suit().compareTo(o.suit());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && value == card.value;
    }

    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(value.code()) + suit.code();
    }

    public static Card fromCodes(String code) {
        if (code == null || code.length() != 2)
            throw new IllegalArgumentException("Invalid card code");
        return create(CardValue.fromCode(code.charAt(0)), CardSuit.fromCode(code.charAt(1)));
    }

    public static Card create(CardValue value, CardSuit suit) {
        return new Card(value, suit);
    }
}