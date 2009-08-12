package euler;

import com.mycila.Collatz;

/**
 * http://projecteuler.net/index.php?section=problems&id=14
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem014 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Collatz sequence = Collatz.from(999999L);
        for (long i = 999998; i > 1; i--) {
            Collatz seq = Collatz.from(i);
            if (seq.size() > sequence.size()) sequence = seq;
        }
        System.out.println("Collatz(" + sequence.startNumber() + ") has " + sequence.size() + " terms, in " + (System.currentTimeMillis() - time) + "ms\n" + sequence);
    }
}

// 837799
