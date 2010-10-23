package com.mycila.jdbc.tx;

import java.util.Deque;
import java.util.LinkedList;

final class TransactionHolder {

    private static final ThreadLocal<Deque<Transaction<? extends Transacted>>> transactionInfoHolder = new ThreadLocal<Deque<Transaction<? extends Transacted>>>() {
        @Override
        protected Deque<Transaction<? extends Transacted>> initialValue() {
            return new LinkedList<Transaction<? extends Transacted>>();
        }
    };

    private TransactionHolder() {
    }

    static boolean hasTransaction() {
        return !transactionInfoHolder.get().isEmpty();
    }

    static <T extends Transacted> Transaction<T> current() {
        Deque<Transaction<? extends Transacted>> deque = transactionInfoHolder.get();
        if (deque.isEmpty())
            throw new IllegalTransactionStateException("No Transaction bound to local thread");
        return (Transaction<T>) deque.peek();
    }

    static int size() {
        return transactionInfoHolder.get().size();
    }

    static int push(Transaction<?> transaction) {
        transactionInfoHolder.get().offerFirst(transaction);
        return size();
    }

    static <T extends Transacted> Transaction<T> pop() {
        Deque<Transaction<? extends Transacted>> deque = transactionInfoHolder.get();
        if (deque.isEmpty())
            throw new IllegalTransactionStateException("No Transaction bound to local thread");
        return (Transaction<T>) deque.pop();
    }

    static void pop(Transaction<?> transaction) {
        Deque<Transaction<? extends Transacted>> deque = transactionInfoHolder.get();
        deque.remove(transaction);
    }
}
