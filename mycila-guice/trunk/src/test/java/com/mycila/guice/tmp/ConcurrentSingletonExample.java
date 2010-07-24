package com.mycila.guice.tmp;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Stage;

import java.util.concurrent.TimeUnit;

/**
 * An artificial example of using ConcurrentSingletonScope.
 */
public class ConcurrentSingletonExample {

    public static void main(String... args) throws InterruptedException {
        long start = System.nanoTime();

        Injector injector = Guice.createInjector(Stage.PRODUCTION,
            new AbstractModule() {
                public void configure() {
                    bind(A.class);
                    bind(B.class);
                    bind(C.class);
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