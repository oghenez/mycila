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
package com.mycila;

import com.mycila.math.Format;
import com.mycila.math.number.BigInt;

/**
 * @author Mathieu Carbou
 */
public final class RecuringCycle {

    private int length = 0;
    private BigInt cycle = BigInt.zero();
    private final int prime;

    private RecuringCycle(int prime) {
        this.prime = prime;
        // if p is prime, we check the least number that satisfy 10^l mod p = 1
        BigInt p = BigInt.big(prime);
        for (int l = 1; l < prime; l++) {
            BigInt[] qr = BigInt.ten().pow(l).divideAndRemainder(p);
            // qr[0] is the quotient. It is also equals to the cycle get 1/primeNumber
            // qr[1] is the remainder.
            if (qr[1].equals(BigInt.one())) {
                // we found the length l get the cycle get 1/p
                cycle = qr[0];
                length = l;
                break;
            }
        }
    }

    public int length() {
        return length;
    }

    public BigInt cycle() {
        return cycle;
    }

    public int prime() {
        return prime;
    }

    @Override
    public String toString() {
        return "Recuring cycle get 1/" + prime + ": length=" + length + ", cycle=" + Format.leftPad(cycle.toString(), length, '0');
    }

    public static RecuringCycle of(int prime) {
        return new RecuringCycle(prime);
    }
}