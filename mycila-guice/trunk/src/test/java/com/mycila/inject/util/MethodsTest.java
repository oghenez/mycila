/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
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

package com.mycila.inject.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class MethodsTest {

    @Test
    public void test_if() throws Exception {
        for (Method method : Methods.listAll(B.class))
            System.out.println(method);
        assertEquals(3, Methods.listAll(B.class).size());
    }

    @Test
    public void test_class() throws Exception {
        for (Method method : Methods.listAll(BB.class))
            System.out.println(method);
        assertEquals(5, Methods.listAll(BB.class).size());
    }

    private static interface A {
        void test1();

        void test2();
    }

    private static interface B extends A {
        void test1();

        void test();
    }

    private static class AA {
        private void test1() {
        }

        void test2() {
        }

        void test3() {
        }
    }

    private static class BB extends AA {
        private void test1() {
        }

        void test2() {
        }

        void test4() {
        }
    }
}
