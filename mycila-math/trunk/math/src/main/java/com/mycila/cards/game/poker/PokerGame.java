package com.mycila.cards.game.poker;

import com.mycila.cards.Game;
import com.mycila.cards.Hand;
import com.mycila.cards.HandRankFactory;

/**
 * @author Mathieu Carbou
 */
public final class PokerGame extends Game {
    @Override
    protected HandRankFactory handRankFactory(Hand hand) {
        return RankType.getHigherRank(hand);
    }
}
