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

import com.mycila.math.distribution.Distribution;
import com.mycila.math.distribution.Item;
import com.mycila.math.distribution.SortOrder;

import static java.lang.System.*;
import java.util.Scanner;

/**
 * http://projecteuler.net/index.php?section=problems&id=59
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem059 {

    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();

        // hold the distribution of characters for each encoding ascii code
        Distribution<Integer>[] distributions = new Distribution[]{
                Distribution.of(Integer.class),
                Distribution.of(Integer.class),
                Distribution.of(Integer.class)};


        // get the frequencies of the ciphered text
        Scanner scanner = new Scanner(Problem059.class.getResourceAsStream("/cipher1.txt")).useDelimiter(",");

        for (int i = 0; scanner.hasNext(); i++)
            distributions[i % 3].add(scanner.nextInt());

        // output thse frequencies
        for (Distribution<Integer> distribution : distributions) {
            System.out.println("Number of different ASCII codes: " + distribution.size());
            System.out.println("Letter\tCount\t%");
            for (Item<Integer> item : distribution.sortByCount(SortOrder.DESC)) {
                System.out.println(item.value() + "\t" + item.count() + "\t" + (100f * item.count() / distribution.totalSize()));
            }
            System.out.println();
        }

        // decoding essay
        int count = 0;
        scanner = new Scanner(Problem059.class.getResourceAsStream("/cipher1.txt")).useDelimiter(",");
        for (int i = 0; scanner.hasNext(); i++) {
            int c = scanner.nextInt();
            if (i % 3 == 0) c ^= 103;
            else if ((i + 2) % 3 == 0) c ^= 111;
            else c ^= 100;
            count += c;
            System.out.print((char) c);
        }
        System.out.println('\n');

        out.println(count + " in " + (currentTimeMillis() - time) + "ms");
    }

}

/*

We can use frequency analysis (http://en.wikipedia.org/wiki/Frequency_analysis) to decrypt the text. The english letter frequencies (http://en.wikipedia.org/wiki/Letter_frequencies) shows that spaces are twice as more frequent as letter 'e'.

As we have 3 encoding characters, we must split the cipher text to find frequencies for each characters.

For the first encoding character:

Number of different ASCII codes: 40
Letter	Count	%
71	70	17.45636
2	48	11.970075
9	28	6.9825435
19	28	6.9825435
[...]

For the second:

Number of different ASCII codes: 35
Letter	Count	%
79	85	21.25
7	35	8.75
10	31	7.75
6	28	7.0
[...]

For the third:

Number of different ASCII codes: 41
Letter	Count	%
68	77	19.25
1	41	10.25
16	31	7.75
11	26	6.5
[...]

As we can see, the first encryptd letter 68 is nearly twice the second one. So 71, 79 and 68 are likely to be the space character (' '=32) encrypted by each character of the key.

71 XOR 32 = 103 (letter 'g'). So the first lower case character could be 'g'
79 XOR 32 = 111 (letter 'o'). So the second lower case character could be 'o'
68 XOR 32 = 100 (letter 'd'). So the third lower case character could be 'd'

=> We found our encryption key: 'god'.

If we try to decrypt it, it works and we find... "The Gospel of John, chapter 1"

*/