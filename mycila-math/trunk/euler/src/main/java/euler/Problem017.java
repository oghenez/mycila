package euler;

import com.mycila.Format;

/**
 * http://projecteuler.net/index.php?section=problems&id=17
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem017 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 1000; i++) sb.append(Format.asWords(i)).append("\n");
        System.out.println(sb.toString().replaceAll("\\s", "").length() + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}

// 21124
