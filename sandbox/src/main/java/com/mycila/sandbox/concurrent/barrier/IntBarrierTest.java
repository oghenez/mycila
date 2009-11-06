package com.mycila.sandbox.concurrent.barrier;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mycila.sandbox.Log.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class IntBarrierTest {

    @Test
    public void test_threads_wait_for_barrier() throws Exception {
        final int nThreads = 15;
        final Queue<Thread> threads = new ConcurrentLinkedQueue<Thread>();
        final CountDownLatch go = new CountDownLatch(1);

        // create a value starting from 0
        final IntBarrier barrier = IntBarrier.zero();

        // create and starting threads, each waiting on a value to happen
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    final int n = new Random().nextInt(20) + 1;
                    try {
                        go.await();
                        log("Waiting for value %s", n);
                        barrier.waitFor(n);
                    } catch (InterruptedException e) {
                        log("Interrupted !");
                        Thread.currentThread().interrupt();
                    }
                    log("Released !");
                    int c = barrier.increment();
                    log("Incremented counter to " + c);
                    threads.remove(Thread.currentThread());
                }
            }, "T-" + (i + 1)) {
                public String toString() {
                    return getName();
                }
            };
            threads.add(t);
            t.start();
        }

        // while there are some threads remaining, increment the value
        go.countDown();
        while (!threads.isEmpty()) {
            log("Remaining threads: " + threads);
            int c = barrier.increment();
            log("Incremented counter to " + c);
        }
    }

    @Test
    public void test_producer_consumer() throws Exception {
        final int nThreads = 10;

        // numbers to process will be put here. we don't know how many numbers there will be
        final BlockingQueue<Integer> itemsToProcess = new LinkedBlockingQueue<Integer>();
        final AtomicInteger sumOfPrimeFactorsBelow20 = new AtomicInteger(0);
        final Queue<Thread> processors = new ConcurrentLinkedQueue<Thread>();

        // create a barrier to wait for thread completion
        final IntBarrier barrier = IntBarrier.zero();

        // create and starting threads, each waiting for a number to process
        // each thread act as a producer & consumer
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            //log("Waiting for number");
                            barrier.increment();
                            int n = itemsToProcess.take();
                            barrier.decrement();
                            //log("Processing: %s", n);
                            if (BigInteger.valueOf(n).isProbablePrime(100)) {
                                //log("%s is prime", n);
                                if (n < 20)
                                    sumOfPrimeFactorsBelow20.addAndGet(n);
                            } else {
                                // decompose n
                                int factor;
                                int q;
                                if (n % 2 == 0) {
                                    factor = 2;
                                    q = n / 2;
                                } else {
                                    factor = 3;
                                    while (n % factor != 0)
                                        factor += 2;
                                    q = n / factor;
                                }
                                //log("%s decomposed to %s x %s", n, factor, q);
                                itemsToProcess.offer(factor);
                                itemsToProcess.offer(q);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }, "T-" + (i + 1)) {
                public String toString() {
                    return getName();
                }
            };
            processors.add(t);
            t.start();
        }

        for (int i = 2; i <= 1000; i++)
            itemsToProcess.add(i);

        // wait for all threads to finish
        while (!itemsToProcess.isEmpty()) {
            barrier.waitFor(nThreads);
            //log("All threads are waiting");
        }

        log("Finished ! Sum of prime factors < 20 of all numbers from 2 to 100 = " + sumOfPrimeFactorsBelow20.get());

        // kill processors
        for (Thread processor : processors)
            processor.interrupt();
    }

}

/*

Output sample:

2009-11-03 22:32:31.465	[T-1] Waiting for value 9
2009-11-03 22:32:31.468	[T-2] Waiting for value 6
2009-11-03 22:32:31.469	[T-4] Waiting for value 11
2009-11-03 22:32:31.469	[T-5] Waiting for value 18
2009-11-03 22:32:31.47	[T-6] Waiting for value 7
2009-11-03 22:32:31.47	[T-3] Waiting for value 2
2009-11-03 22:32:31.479	[T-7] Waiting for value 15
2009-11-03 22:32:31.48	[T-15] Waiting for value 2
2009-11-03 22:32:31.48	[T-14] Waiting for value 9
2009-11-03 22:32:31.481	[T-13] Waiting for value 9
2009-11-03 22:32:31.481	[T-12] Waiting for value 7
2009-11-03 22:32:31.481	[T-11] Waiting for value 7
2009-11-03 22:32:31.482	[T-10] Waiting for value 6
2009-11-03 22:32:31.482	[T-9] Waiting for value 3
2009-11-03 22:32:31.482	[T-8] Waiting for value 0
2009-11-03 22:32:31.483	[T-8] Released !
2009-11-03 22:32:31.483	[T-8] Incrementing counter to 1
2009-11-03 22:32:32.463	[main] Remaining threads: [T-1, T-2, T-3, T-4, T-5, T-6, T-7, T-9, T-10, T-11, T-12, T-13, T-14, T-15]
2009-11-03 22:32:32.465	[main] Incrementing counter to 2
2009-11-03 22:32:32.466	[T-15] Released !
2009-11-03 22:32:32.466	[T-15] Incrementing counter to 3
2009-11-03 22:32:32.467	[T-3] Released !
2009-11-03 22:32:32.468	[T-3] Incrementing counter to 4
2009-11-03 22:32:32.469	[T-9] Released !
2009-11-03 22:32:32.47	[T-9] Incrementing counter to 5
2009-11-03 22:32:33.466	[main] Remaining threads: [T-1, T-2, T-4, T-5, T-6, T-7, T-10, T-11, T-12, T-13, T-14]
2009-11-03 22:32:33.467	[main] Incrementing counter to 6
2009-11-03 22:32:33.468	[T-10] Released !
2009-11-03 22:32:33.469	[T-10] Incrementing counter to 7
2009-11-03 22:32:33.47	[T-6] Released !
2009-11-03 22:32:33.47	[T-6] Incrementing counter to 8
2009-11-03 22:32:33.471	[T-12] Released !
2009-11-03 22:32:33.472	[T-12] Incrementing counter to 9
2009-11-03 22:32:33.473	[T-11] Released !
2009-11-03 22:32:33.473	[T-11] Incrementing counter to 10
2009-11-03 22:32:33.474	[T-2] Released !
2009-11-03 22:32:33.475	[T-2] Incrementing counter to 11
2009-11-03 22:32:33.476	[T-13] Released !
2009-11-03 22:32:33.477	[T-13] Incrementing counter to 12
2009-11-03 22:32:33.477	[T-4] Released !
2009-11-03 22:32:33.478	[T-4] Incrementing counter to 13
2009-11-03 22:32:33.481	[T-14] Released !
2009-11-03 22:32:33.481	[T-14] Incrementing counter to 14
2009-11-03 22:32:33.482	[T-1] Released !
2009-11-03 22:32:33.483	[T-1] Incrementing counter to 15
2009-11-03 22:32:33.484	[T-7] Released !
2009-11-03 22:32:33.485	[T-7] Incrementing counter to 16
2009-11-03 22:32:34.468	[main] Remaining threads: [T-5]
2009-11-03 22:32:34.468	[main] Incrementing counter to 17
2009-11-03 22:32:35.469	[main] Remaining threads: [T-5]
2009-11-03 22:32:35.47	[main] Incrementing counter to 18
2009-11-03 22:32:35.471	[T-5] Released !
2009-11-03 22:32:35.471	[T-5] Incrementing counter to 19
2009-11-03 22:32:36.471	[main] Remaining threads: []
2009-11-03 22:32:36.472	[main] Incrementing counter to 20

*/