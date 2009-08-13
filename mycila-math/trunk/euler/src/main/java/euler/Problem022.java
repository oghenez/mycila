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
        Scanner scanner = new Scanner(Problem022.class.getResourceAsStream("/names.txt"));
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