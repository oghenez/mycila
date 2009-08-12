package euler;

import com.mycila.Pythagore;
import com.mycila.Triplet;

import static java.lang.System.*;
import java.util.List;

/**
 * http://projecteuler.net/index.php?section=problems&id=39
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem039 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        List<Triplet> max = Pythagore.triplet(1);
        for (int n = 2; n <= 1000; n++) {
            List<Triplet> triplets = Pythagore.triplet(n);
            if (triplets.size() > max.size()) max = triplets;
        }
        out.println(max.get(0).sum() + " : " + max + " in " + (currentTimeMillis() - time) + "ms");
    }
}
