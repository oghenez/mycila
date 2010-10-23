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

import java.util.Arrays;

final class DefaultTransactionDefinition implements TransactionDefinition {

    Propagation propagation = Propagation.REQUIRED;
    Isolation isolation = Isolation.DEFAULT;
    boolean readOnly = false;
    Class<?>[] rollbackOn = new Class[]{Throwable.class};

    @Override
    public Propagation getPropagation() {
        return propagation;
    }

    @Override
    public Isolation getIsolationLevel() {
        return isolation;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public boolean rollbackOn(Throwable ex) {
        for (Class<?> c : rollbackOn)
            if (c.isInstance(ex))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "TransactionDefinition{" +
                "isolation=" + isolation +
                ", propagation=" + propagation +
                ", readOnly=" + readOnly +
                ", rollback=" + (rollbackOn == null ? null : Arrays.asList(rollbackOn)) +
                '}';
    }
}
