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

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentThrowingTest {

    @Rule
    public ConcurrentRule concurrentRule = new ConcurrentRule();

    @Test
    @Concurrency(10)
    public void myTestMethod() throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getName() + " started !");
        int n = new Random().nextInt(5000);
        System.out.println("Thread " + Thread.currentThread().getName() + " wait " + n + "ms");
        Thread.sleep(n);
        System.out.println("Thread " + Thread.currentThread().getName() + " finished");
    }

    @Test(expected = NumberFormatException.class)
    @Concurrency(10)
    public void myTestMethod_expect() throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getName() + " started !");
        int n = new Random().nextInt(5000);
        System.out.println("Thread " + Thread.currentThread().getName() + " wait " + n + "ms");
        Thread.sleep(n);
        Integer.parseInt("blabla");
    }

    @Test
    @Concurrency(10)
    @Ignore
    public void myTestMethod_failing() throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getName() + " started !");
        int n = new Random().nextInt(5000);
        System.out.println("Thread " + Thread.currentThread().getName() + " wait " + n + "ms");
        Thread.sleep(n);
        Integer.parseInt("blabla");
    }

    @Test
    public void non_concurrent() throws InterruptedException {
        System.out.println("myTestMethod_failing Thread " + Thread.currentThread().getName() + " started !");
        int n = new Random().nextInt(1000);
        System.out.println("myTestMethod_failing Thread " + Thread.currentThread().getName() + " wait " + n + "ms");
        Thread.sleep(n);
        System.out.println("myTestMethod_failing Thread " + Thread.currentThread().getName() + " finished");
    }

}