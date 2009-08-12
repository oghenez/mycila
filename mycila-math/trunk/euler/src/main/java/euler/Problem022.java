package euler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * http://projecteuler.net/index.php?section=problems&id=22
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem022 {
    public static void main(String[] args) throws FileNotFoundException {
        long time = System.currentTimeMillis();
        List<String> names = new ArrayList<String>();
        Scanner scanner = new Scanner(new File("data/names.txt"));
        scanner.useDelimiter(",");
        while (scanner.hasNext()) names.add(scanner.next().toUpperCase());
        Collections.sort(names);
        long total = 0;
        for (int pos = 1; pos <= names.size(); pos++) {
            for (int i = 0; i < names.get(pos - 1).length(); i++)
                total += pos * (names.get(pos - 1).charAt(i) - 64);
        }
        System.out.println(total + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}