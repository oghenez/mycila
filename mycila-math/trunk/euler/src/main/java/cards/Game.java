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
