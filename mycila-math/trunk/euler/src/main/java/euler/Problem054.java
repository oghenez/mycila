package euler;

import com.mycila.cards.Hand;
import com.mycila.cards.HandRank;
import com.mycila.cards.game.poker.PokerGame;

import java.io.File;
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
        Scanner scanner = new Scanner(new File("data/poker.txt"));
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
