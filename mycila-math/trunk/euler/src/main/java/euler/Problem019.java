package euler;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

/**
 * http://projecteuler.net/index.php?section=problems&id=19
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem019 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        int sum = 0;
        for (LocalDate date = new LocalDate(1901, 1, 1), max = new LocalDate(2000, 12, 31); date.isBefore(max); date = date.plusMonths(1))
            if (date.getDayOfWeek() == DateTimeConstants.SUNDAY)
                sum++;
        System.out.println(sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}
