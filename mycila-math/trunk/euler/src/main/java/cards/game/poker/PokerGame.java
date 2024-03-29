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
package cards.game.poker;

import cards.Game;
import cards.Hand;
import cards.HandRankFactory;

/**
 * @author Mathieu Carbou
 */
public final class PokerGame extends Game {
    @Override
    protected HandRankFactory handRankFactory(Hand hand) {
        return RankType.getHigherRank(hand);
    }
}
