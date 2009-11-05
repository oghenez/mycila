package com.mycila.sandbox.junit;

import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentTest {

    @Rule
    public ConcurrentRule concurrentRule = new ConcurrentRule();

    @Test
    @Concurrent(15)
    public void myTestMethod() throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getName() + " started !");
        int n = new Random().nextInt(5000);
        System.out.println("Thread " + Thread.currentThread().getName() + " wait " + n + "ms");
        Thread.sleep(n);
        System.out.println("Thread " + Thread.currentThread().getName() + " finished");
    }

    @Test
    public void non_concurrent() throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getName() + " started !");
        int n = new Random().nextInt(1000);
        System.out.println("Thread " + Thread.currentThread().getName() + " wait " + n + "ms");
        Thread.sleep(n);
        System.out.println("Thread " + Thread.currentThread().getName() + " finished");
    }
    
}
