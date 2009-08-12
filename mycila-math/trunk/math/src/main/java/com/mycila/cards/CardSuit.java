package com.mycila.cards;

/**
 * @author Mathieu Carbou
 */
public enum CardSuit {

    CLUB('C'),
    DIAMOND('D'),
    HEART('H'),
    SPADE('S'),;

    private final char code;

    private CardSuit(char code) {
        this.code = code;
    }

    public char code() {
        return code;
    }

    public static CardSuit fromCode(char code) {
        for (CardSuit value : values()) if (value.code() == code) return value;
        throw new IllegalArgumentException("Not a valid card suit: " + code);
    }
}
