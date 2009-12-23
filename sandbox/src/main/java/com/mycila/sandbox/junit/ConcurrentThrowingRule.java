package com.mycila.sandbox.junit;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentThrowingRule implements MethodRule {
    @Override
    public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Concurrent concurrent = frameworkMethod.getAnnotation(Concurrent.class);
                if (concurrent == null)
                    statement.evaluate();
                else {
                    // create an executor which cimply spawns threads to execute runnables
                    Executor executor = new Executor() {
                        final String name = frameworkMethod.getName();
                        int count = 0;

                        @Override
                        public void execute(Runnable command) {
                            new Thread(command, name + "-Thread-" + count++).start();
                        }
                    };
                    // create a completion service to get jobs in the order they finish, to be able
                    // to cancel remaining jobs as fast as possible if an exception occurs
                    CompletionService<Void> completionService = new ExecutorCompletionService<Void>(executor);
                    // latch used to pause all threads and start all of them (nearly) at the same time
                    final CountDownLatch go = new CountDownLatch(1);
                    // create the tasks
                    for (int i = 0; i < concurrent.value(); i++) {
                        completionService.submit(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                try {
                                    go.await();
                                    statement.evaluate();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                } catch (Throwable throwable) {
                                    if (throwable instanceof Exception)
                                        throw (Exception) throwable;
                                    if (throwable instanceof Error)
                                        throw (Error) throwable;
                                    // case of exceptions directly subclassing Throwable
                                    // (should not occur - bad programming)
                                    RuntimeException r = new RuntimeException(throwable.getMessage(), throwable);
                                    r.setStackTrace(throwable.getStackTrace());
                                    throw r;
                                }
                                return null;
                            }
                        });
                    }
                    go.countDown();
                    Throwable throwable = null;
                    for (int i = 0; i < concurrent.value(); i++) {
                        try {
                            completionService.take().get();
                        } catch (ExecutionException e) {
                            // only keep the first exception, but wait for all threads to finish
                            if(throwable == null)
                                throwable = e.getCause();
                        }
                    }
                    if(throwable != null) throw throwable;
                }
            }
        };
    }
}