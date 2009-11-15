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

package com.mycila.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class SignatureTest {

    @Test
    public void test() throws Exception {
        for (Method method : DefaultDispatcher.class.getDeclaredMethods())
            System.out.println(MethodSignature.of(method));
        for (Method method : ErrorHandlers.class.getDeclaredMethods())
            System.out.println(MethodSignature.of(method));
        for (Method method : SignatureTest.class.getDeclaredMethods())
            System.out.println(MethodSignature.of(method));
    }

    @Test
    public void test2() throws Exception {
        for (Method method : ClassUtils.getAllDeclaredMethods(SignatureTest.class))
            System.out.println(MethodSignature.of(method));
        for (Method method : ClassUtils.getAllDeclaredMethods(MethodSignature.class))
            System.out.println(MethodSignature.of(method));
        for (Method method : ClassUtils.getAllDeclaredMethods(ErrorHandlers.class))
            System.out.println(MethodSignature.of(method));
        for (Method method : ClassUtils.getAllDeclaredMethods(DefaultDispatcher.class))
            System.out.println(MethodSignature.of(method));
        /*for (Method method : AnnotationProcessor.getAllDeclaredMethods(JButton.class))
            System.out.println(MethodSignature.of(method));*/
    }

    static void test_array(Class<?>[]... a) {
    }

    static void test_int(int a) {
    }

    static void test_int(int[][] a) {
    }

    void test_int(int[] a) {
    }

}
