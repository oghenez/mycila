/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.junit.concurrent;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(ConcurrentJunitRunner.class)
@Concurrency(5)
public final class ATest extends TestSkeleton {

    @Test public void test0() throws Throwable { printAndWait(); }
    @Test public void test1() throws Throwable { printAndWait(); }
    @Test public void test2() throws Throwable { printAndWait(); }
    @Test public void test3() throws Throwable { printAndWait(); }
    @Test public void test4() throws Throwable { printAndWait(); }
    @Test public void test5() throws Throwable { printAndWait(); }
    @Test public void test6() throws Throwable { printAndWait(); }
    @Test public void test7() throws Throwable { printAndWait(); }
    @Test public void test8() throws Throwable { printAndWait(); }
    @Test public void test9() throws Throwable { printAndWait(); }

    public static void main(String[] args) {
        JUnitCore.main(ATest.class.getName());
    }

}
