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

public interface Transacted {
    boolean isRollbackOnly();

    void setRollbackOnly();

    /**
     * Create a new savepoint. You can roll back to a specific savepoint
     * via <code>rollbackToSavepoint</code>, and explicitly release a
     * savepoint that you don't need anymore via <code>releaseSavepoint</code>.
     * <p>Note that most transaction managers will automatically release
     * savepoints at transaction completion.
     *
     * @return a savepoint object, to be passed into rollbackToSavepoint
     *         or releaseSavepoint
     * @throws TransactionException if the savepoint could not be created,
     *                              for example because the transaction is not in an appropriate state
     * @see java.sql.Connection#setSavepoint
     */
    Object createSavepoint() throws TransactionException;

    /**
     * Roll back to the given savepoint. The savepoint will be
     * automatically released afterwards.
     *
     * @param savepoint the savepoint to roll back to
     * @throws TransactionException if the rollback failed
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    void rollbackToSavepoint(Object savepoint) throws TransactionException;

    /**
     * Explicitly release the given savepoint.
     * <p>Note that most transaction managers will automatically release
     * savepoints at transaction completion.
     * <p>Implementations should fail as silently as possible if
     * proper resource cleanup will still happen at transaction completion.
     *
     * @param savepoint the savepoint to release
     * @throws TransactionException if the release failed
     * @see java.sql.Connection#releaseSavepoint
     */
    void releaseSavepoint(Object savepoint) throws TransactionException;

}
