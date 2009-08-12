package com.mycila.cards;

/**
 * @author Mathieu Carbou
 */
public enum CardValue {

    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    HEIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    private final char code;

    private CardValue(char code) {
        this.code = code;
    }

    public char code() {
        return code;
    }

    public static CardValue fromCode(char code) {
        for (CardValue value : values()) if (value.code() == code) return value;
        throw new IllegalArgumentException("Not a valid card value: " + code);
    }
}
