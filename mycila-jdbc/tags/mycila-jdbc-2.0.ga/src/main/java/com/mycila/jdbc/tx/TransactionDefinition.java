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

/**
 * Interface that defines Spring-compliant transaction properties.
 * Based on the propagation behavior definitions analogous to EJB CMT attributes.
 * <p/>
 * <p>Note that isolation level and timeout settings will not get applied unless
 * an actual new transaction gets started. As only {@link Propagation#REQUIRED},
 * {@link Propagation#REQUIRES_NEW} and {@link Propagation#NESTED} can cause
 * that, it usually doesn't make sense to specify those settings in other cases.
 * Furthermore, be aware that not all transaction managers will support those
 * advanced features and thus might throw corresponding exceptions when given
 * non-default values.
 * <p/>
 * <p>The {@link #isReadOnly() read-only flag} applies to any transaction context,
 * whether backed by an actual resource transaction or operating non-transactionally
 * at the resource level. In the latter case, the flag will only apply to managed
 * resources within the application, such as a Hibernate <code>Session</code>.
 *
 * @author Juergen Hoeller
 * @since 08.05.2003
 */
public interface TransactionDefinition {

    /**
     * Return the propagation behavior.
     * <p>Must return one of the <code>PROPAGATION_XXX</code> constants
     * defined on {@link TransactionDefinition this interface}.
     *
     * @return the propagation behavior
     */
    Propagation getPropagation();

    /**
     * Return the isolation level.
     * <p>Must return one of the <code>ISOLATION_XXX</code> constants
     * defined on {@link TransactionDefinition this interface}.
     * <p>Only makes sense in combination with {@link Propagation#REQUIRED}
     * or {@link Propagation#REQUIRES_NEW}.
     * <p>Note that a transaction manager that does not support custom isolation levels
     * will throw an exception when given any other level than {@link Isolation#DEFAULT}.
     *
     * @return the isolation level
     */
    Isolation getIsolationLevel();

    /**
     * Return whether to optimize as a read-only transaction.
     * <p>The read-only flag applies to any transaction context, whether
     * backed by an actual resource transaction
     * ({@link Propagation#REQUIRED}/{@link Propagation#REQUIRES_NEW}) or
     * operating non-transactionally at the resource level
     * ({@link Propagation#SUPPORTS}). In the latter case, the flag will
     * only apply to managed resources within the application, such as a
     * Hibernate <code>Session</code>.
     * <p>This just serves as a hint for the actual transaction subsystem;
     * it will <i>not necessarily</i> cause failure of write access attempts.
     * A transaction manager which cannot interpret the read-only hint will
     * <i>not</i> throw an exception when asked for a read-only transaction.
     *
     * @return <code>true</code> if the transaction is to be optimized as read-only
     */
    boolean isReadOnly();

    /**
     * Should we roll back on the given exception?
     *
     * @param ex the exception to evaluate
     * @return whether to perform a rollback or not
     */
    boolean rollbackOn(Throwable ex);
}
