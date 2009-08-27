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

import com.mycila.math.number.BigInt;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class RecuringCycleTest {

    @Test
    public void test() {
        RecuringCycle recuringCycle = RecuringCycle.of(2);
        assertEquals(recuringCycle.length(), 0);
        assertEquals(recuringCycle.cycle(), BigInt.zero());
        System.out.println(recuringCycle);

        recuringCycle = RecuringCycle.of(5);
        assertEquals(recuringCycle.length(), 0);
        assertEquals(recuringCycle.cycle(), BigInt.zero());
        System.out.println(recuringCycle);

        recuringCycle = RecuringCycle.of(3);
        assertEquals(recuringCycle.length(), 1);
        assertEquals(recuringCycle.cycle(), BigInt.big(3));
        System.out.println(recuringCycle);

        recuringCycle = RecuringCycle.of(983);
        assertEquals(recuringCycle.length(), 982);
        assertEquals(recuringCycle.cycle().toString(), "10172939979654120040691759918616480162767039674465920651068158697863682604272634791454730417090539165818921668362156663275686673448626653102746693794506612410986775178026449643947100712105798575788402848423194303153611393692777212614445574771108850457782299084435401831129196337741607324516785350966429298067141403865717192268565615462868769074262461851475076297049847405900305188199389623601220752797558494404883011190233977619532044760935910478128179043743641912512716174974567650050864699898270600203458799593082400813835198372329603255340793489318413021363173957273652085452695829094608341810783316378433367243133265513733468972533062054933875890132248219735503560528992878942014242115971515768056968463886063072227873855544252288911495422177009155645981688708036622583926754832146490335707019328585961342828077314343845371312309257375381485249237029501525940996948118006103763987792472024415055951169888097660223804679552390640895218718209562563580874872838250254323499491353");
        System.out.println(recuringCycle);
    }

}

