package com.mycila.cards;

/**
 * @author Mathieu Carbou
 */
public abstract class Game {

    public final Hand handEmpty() {
        return new Hand(this);
    }

    public final Hand handWith(Card... cards) {
        return handEmpty().add(cards);
    }

    public final Hand handFromCodes(String codes) {
        return handFromCodes(codes.split("\\s|;|,|:"));
    }

    public final Hand handFromCodes(String... codes) {
        Hand hand = handEmpty();
        for (String code : codes) {
            if (code != null && code.length() == 2) {
                hand.add(Card.fromCodes(code));
            }
        }
        return hand;
    }

    public final Hand handFrom(Hand hand) {
        return handEmpty().add(hand);
    }

    protected abstract HandRankFactory handRankFactory(Hand hand);

}
