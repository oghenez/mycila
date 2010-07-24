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

package com.mycila.guice.tmp;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

import java.util.concurrent.TimeUnit;

/**
 * An artificial example of using ConcurrentSingletonScope.
 */
public class ConcurrentSingletonExample {

    public static void main(String... args) throws InterruptedException {
        long start = System.nanoTime();

        Injector injector = Guice.createInjector(
            new AbstractModule() {
                public void configure() {
                    ConcurrentSingletonScope.install(binder());
                }
            }
        );

        A a = injector.getInstance(A.class);

        long elapsed = System.nanoTime() - start;

        System.out.printf("Completed in %d seconds%n", TimeUnit.NANOSECONDS.toSeconds(elapsed));
    }

    // Concurrently provided "services" A, B, and C. A depends on B and C.

    @ConcurrentSingleton
    static class A {
        @Inject public A(Provider<B> b, Provider<C> c) {
            try {
                System.out.printf("Starting A on thread %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                System.out.printf("A getting B and C instances on thread %s%n", Thread.currentThread().getName());
                b.get();
                c.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Started A");
            }
        }
    }

    @ConcurrentSingleton
    static class B {
        @Inject public B() {
            try {
                System.out.printf("Starting B on thread %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Started B");
            }
        }
    }

    @ConcurrentSingleton
    static class C {
        @Inject public C() {
            try {
                System.out.printf("Starting C on thread %s%n", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Started C");
            }
        }
    }
}