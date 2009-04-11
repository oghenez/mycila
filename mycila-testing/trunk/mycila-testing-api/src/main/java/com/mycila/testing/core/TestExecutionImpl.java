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

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class TestExecutionImpl implements TestExecution {

    private final ExecutionImpl execution;
    private boolean mustSkip;

    TestExecutionImpl(Context context, Method method) {
        execution = new ExecutionImpl(context, method);
    }

    public boolean mustSkip() {
        return mustSkip;
    }

    public void setSkip(boolean mustSkip) {
        this.mustSkip = mustSkip;
    }

    TestExecutionImpl changeStep(Step step) {
        execution.changeStep(step);
        return this;
    }

    public Step step() {
        return execution.step();
    }

    public Method method() {
        return execution.method();
    }

    public Context context() {
        return execution.context();
    }

    public Throwable throwable() {
        return execution.throwable();
    }

    public boolean hasFailed() {
        return execution.hasFailed();
    }
    public void setThrowable(Throwable throwable) {
        execution.setThrowable(throwable);
    }
}
