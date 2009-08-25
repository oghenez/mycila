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
package com.mycila.math;

import com.mycila.math.triplet.IntTriplet;
import com.mycila.math.triplet.Triplet;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

import static java.math.BigInteger.*;

/**
 * @author Mathieu Carbou
 */
public final class EuclidTest {

    @Test
    public void test_extended() {
        Assert.assertEquals(Euclid.extended(352, 168), IntTriplet.of(-10, 21, 8));
        assertEquals(Euclid.extended(168, 352), IntTriplet.of(21, -10, 8));
        assertEquals(Euclid.extended(3458, 4864), IntTriplet.of(-45, 32, 38));
        assertEquals(Euclid.extended(valueOf(123456789), valueOf(987654321)), Triplet.of(valueOf(-8), valueOf(1), valueOf(9)));
    }

}