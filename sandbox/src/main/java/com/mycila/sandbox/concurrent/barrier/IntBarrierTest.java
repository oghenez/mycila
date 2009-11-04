package com.mycila.sandbox.concurrent.barrier;

import static com.mycila.sandbox.Log.*;
import org.junit.Test;

import java.util.Queue;
import java.util.Random;
import java.util.SortedSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

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
                    final int n = new Random().nextInt(20);
                    try {
                        go.await();
                        log("Waiting for value %s", n);
                        barrier.waitFor(n);
                    } catch (InterruptedException e) {
                        log("Interrupted !");
                        Thread.currentThread().interrupt();
                    }
                    log("Released !");
                    log("Incrementing counter to " + barrier.incrementAndGet());
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
            Thread.sleep(1000);
            log("Remaining threads: " + threads);
            log("Incrementing counter to " + barrier.incrementAndGet());
        }
    }

    @Test
    public void test_decompose() throws Exception {
        final int nThreads = 5;

        // numbers to process will be put here. we don't know how many numbers there will be
        final BlockingQueue<Integer> toDecompose = new LinkedBlockingQueue<Integer>();
        final SortedSet<Integer> primes = new ConcurrentSkipListSet<Integer>();
        final Queue<Thread> processors = new ConcurrentLinkedQueue<Thread>();

        // create a barrier to wait for thread completion
        final IntBarrier barrier = IntBarrier.zero();

        // create and starting threads, each waiting for a number to process
        // each thread act as a producer & consumer
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        log("Waiting for number (%s threads)", barrier.incrementAndGet());
                        try {
                            int n = toDecompose.take();
                            barrier.decrementAndGet();
                            log("Processing: %s", n);
                            // divide b already found primes
                            for (int p : primes)
                                while (n % p == 0) n /= p;
                            if (n > 1) {
                                // simple decompose
                                int d = primes.last();
                                d += d == 2 ? 1 : 2;
                                while (n % d != 0) d += 2;
                                toDecompose.add(d);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
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

        primes.add(2);
        toDecompose.add(60);

        // wait for all threads to finish
        while (!toDecompose.isEmpty()) {
            barrier.waitFor(nThreads);
            log("All threads waiting");
        }

        log("Finished: " + primes);

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