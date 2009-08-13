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

/**
 * http://projecteuler.net/index.php?section=problems&id=31
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem031 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        // The monetary base is composed of 8 'digits'
        // 200 = 200a + 100b + 50c + 20d + 10e + 5f + 2g + 1h
        int count = 2; // for a = 1, a = 0 and b = 2`
        for (int b = 0; b <= 1; b++)
            for (int c = 0; 100 * b + 50 * c <= 200; c++)
                for (int d = 0; 100 * b + 50 * c + 20 * d <= 200; d++)
                    for (int e = 0; 100 * b + 50 * c + 20 * d + 10 * e <= 200; e++)
                        for (int f = 0; 100 * b + 50 * c + 20 * d + 10 * e + 5 * f <= 200; f++)
                            for (int g = 0; 100 * b + 50 * c + 20 * d + 10 * e + 5 * f + 2 * g <= 200; g++)
                                count++;
        System.out.println(count + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}
// 73682
