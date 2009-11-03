package com.mycila.sandbox.concurrent;

import org.testng.annotations.Test;

import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class RunInParallelTest {

    @Test
    public void test() throws InterruptedException {
        System.out.println("SETUP");
        RunInParallel runInParallel = new RunInParallel(50, new RunInParallel.Task() {
            public void run() throws Exception {
                myTestMethod();
            }
        });

        System.out.println("STARTING");
        runInParallel.start();
        System.out.println("STARTED");

        Thread.sleep(1000);

        System.out.println("WAITING");
        runInParallel.awaitTermination();
        System.out.println("FINISHED");
    }

    private void myTestMethod() throws InterruptedException {
        Thread.sleep(new Random().nextInt(5000));
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
}
