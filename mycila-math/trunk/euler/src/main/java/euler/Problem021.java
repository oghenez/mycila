package euler;

import com.mycila.Divisors;

import java.util.HashMap;
import java.util.Map;

/**
 * http://projecteuler.net/index.php?section=problems&id=21
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem021 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Map<Integer, Integer> sumOfDivisors = new HashMap<Integer, Integer>();
        for (int n = 2; n < 10000; n++) {
            int sum = 0;
            for (int d = 1; d < n; d++) if (n % d == 0) sum += d;
            sumOfDivisors.put(n, sum);
        }
        System.out.println(sumOfDivisors);
        System.out.println(Divisors.sum(10000));
        int sumOfAmicables = 0;
        for (Map.Entry<Integer, Integer> entry : sumOfDivisors.entrySet()) {
            if (entry.getKey().equals(entry.getValue())) {
                System.out.println("Perfect number: " + entry.getKey());
                continue;
            }
            Integer sum = sumOfDivisors.get(entry.getValue());
            if (sum != null && sum.equals(entry.getKey())) {
                System.out.println("Amicable number: " + entry.getKey());
                sumOfAmicables += entry.getKey();
            }
        }
        System.out.println(sumOfAmicables + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}
