/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.testing.core;

import com.mycila.testing.core.api.Attributes;
import static com.mycila.testing.core.api.Ensure.*;
import com.mycila.testing.core.api.Execution;
import com.mycila.testing.core.api.Step;
import com.mycila.testing.core.api.TestContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ExecutionImpl implements Execution {

    private final TestContext context;
    private final Method method;
    private Throwable throwable;
    private Step step = Step.UNKNOWN;
    private final Attributes attributes = new AttributesImpl();

    ExecutionImpl(TestContext context, Method method) {
        notNull("Test context", context);
        notNull("Test method", method);
        this.context = context;
        this.method = method;
    }

    public Attributes attributes() {
        return attributes;
    }

    final ExecutionImpl changeStep(Step step) {
        notNull("Step", step);
        this.step = step;
        return this;
    }

    public final Step step() {
        return step;
    }

    public final Method method() {
        return method;
    }

    public final TestContext context() {
        return context;
    }

    public final Throwable throwable() {
        return throwable;
    }

    public final boolean hasFailed() {
        return throwable != null;
    }

    public void setThrowable(Throwable throwable) {
        if (throwable != null && throwable instanceof InvocationTargetException) {
            throwable = ((InvocationTargetException) throwable).getTargetException();
        }
        this.throwable = throwable;
    }
}
