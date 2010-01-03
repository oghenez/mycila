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

package com.mycila.event.spi;

import com.mycila.event.api.Reachability;
import com.mycila.event.api.Referencable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ReferencableCollectionTest {

    @Test
    public void test() {
        ReferencableCollection<Referencable> list = new ReferencableCollection<Referencable>();
        for (int i = 0; i < 20; i++) {
            list.add(new WeakObject());
            list.add(new HardObject());
        }
        System.gc();
        System.gc();
        System.gc();
        int count = 0;
        for (Referencable referencable : list) {
            System.out.println(referencable);
            count++;
        }
        assertEquals(count, 20);
        assertEquals(list.size(), 20);
    }

    private static class WeakObject implements Referencable {
        static int count = 0;
        final int c;

        private WeakObject() {
            c = count++;
        }

        @Override
        public Reachability getReachability() {
            return Reachability.WEAK;
        }

        @Override
        public String toString() {
            return "WeakObject-" + c;
        }
    }

    private static class HardObject implements Referencable {
        static int count = 0;
        final int c;

        private HardObject() {
            c = count++;
        }

        @Override
        public Reachability getReachability() {
            return Reachability.HARD;
        }

        @Override
        public String toString() {
            return "HardObject-" + c;
        }
    }
}
