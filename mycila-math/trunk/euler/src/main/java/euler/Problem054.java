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
package euler;

import cards.Hand;
import cards.HandRank;
import cards.game.poker.PokerGame;

import static java.lang.System.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * http://projecteuler.net/index.php?section=problems&id=54
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem054 {

    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();

        PokerGame game = new PokerGame();
        List<Hand> handsP1 = new ArrayList<Hand>(1000);
        List<Hand> handsP2 = new ArrayList<Hand>(1000);

        // reads poker file
        Scanner scanner = new Scanner(Problem054.class.getResourceAsStream("/poker.txt"));
        while (scanner.hasNext()) {
            String hands = scanner.nextLine();
            handsP1.add(game.handFromCodes(hands.substring(0, 14)));
            handsP2.add(game.handFromCodes(hands.substring(15)));
        }

        // display player hands
        /*for (int i = 0; i < handsP1.size(); i++) {
            System.out.println(handsP1.get(i) + " vs " + handsP2.get(i));
        }*/

        int count = 0;
        for (int i = 0; i < handsP1.size(); i++) {
            HandRank rank1 = handsP1.get(i).rank();
            HandRank rank2 = handsP2.get(i).rank();
            int compare = rank1.compareTo(rank2);
            System.out.println(MessageFormat.format("GAME {0}:\n - P1: {1} - {2}\n - P2: {3} - {4}\n ==> {5} !",
                    i,
                    rank1.currentHand(), rank1.describe(),
                    rank2.currentHand(), rank2.describe(),
                    compare == 0 ? "draw" : compare > 0 ? "P1 wins" : "P2 wins"));
            if (compare > 0) count++;
        }

        out.println(count + " in " + (currentTimeMillis() - time) + "ms");
    }
}
