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

import com.mycila.log.Logger;
import com.mycila.log.Loggers;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the current test context. The context holder contains the current test instance used for each call to
 * plugin methods for preparing instance, before and after test execution.<p/>
 * <p/>
 * You can safely use in your plugin this class to get from anywhere the current test {@link com.mycila.testing.core.Context}
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Mycila {

    private static final Logger LOGGER = Loggers.get(Mycila.class);
    private static final ThreadLocal<Execution> CURRENT_EXECUTION = new InheritableThreadLocal<Execution>();
    private static final Map<Ref, Context> CONTEXTS = new HashMap<Ref, Context>();

    private Mycila() {
    }

    static void registerCurrentExecution(Execution execution) {
        LOGGER.debug("Registering Execution Context {0}#{1} for test {2}#{3,number,#}", execution.step(), execution.method().getName(), execution.context().test().testClass().getName(), execution.context().test().instance().hashCode());
        CURRENT_EXECUTION.set(execution);
    }

    static void registerContext(Context context) {
        LOGGER.debug("Registering Global Test Context for test {0}#{1,number,#}", context.test().testClass().getName(), context.test().instance().hashCode());
        CONTEXTS.put(new Ref(context.test().instance()), context);
    }

    static void unsetCurrentExecution() {
        if (LOGGER.canDebug()) {
            Execution execution = CURRENT_EXECUTION.get();
            if (execution != null) {
                LOGGER.debug("Removing Execution Context {0}#{1} for test {2}#{3,number,#}", execution.step(), execution.method().getName(), execution.context().test().testClass().getName(), execution.context().test().instance().hashCode());
            }
        }
        CURRENT_EXECUTION.remove();
    }

    static void unsetContext(Object testInstance) {
        Ref ref = new Ref(testInstance);
        if (LOGGER.canDebug()) {
            Context context = CONTEXTS.get(ref);
            if (context != null) {
                LOGGER.debug("Removing Global Test Context for test {0}#{1,number,#}", context.test().testClass().getName(), context.test().instance().hashCode());
            }
        }
        CONTEXTS.remove(ref);
    }

    public static Context context(Object testInstance) {
        Context context = CONTEXTS.get(new Ref(testInstance));
        if (context == null) {
            throw new IllegalStateException("No Global Test Context available for test " + MessageFormat.format("{0}#{1,number,#}", testInstance.getClass().getName(), testInstance.hashCode()));
        }
        return context;
    }

    /**
     * @return The current execution point
     */
    @SuppressWarnings({"unchecked"})
    public static Execution currentExecution() {
        Execution c = CURRENT_EXECUTION.get();
        if (c == null) {
            throw new IllegalStateException("No Execution context bound to local thread !");
        }
        return c;
    }

    private static class Ref {
        final Object object;
        Ref(Object object) {
            this.object = object;
        }
        @Override
        public boolean equals(Object o) {
            return o instanceof Ref && ((Ref) o).object == object;
        }
        @Override
        public int hashCode() {
            return object.hashCode();
        }
    }
}
