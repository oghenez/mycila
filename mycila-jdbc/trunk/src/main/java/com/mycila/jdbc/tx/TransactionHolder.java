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
