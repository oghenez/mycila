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
package com.mycila.log.nop;

import com.mycila.log.AbstractLogger;
import com.mycila.log.Level;
import com.mycila.log.Logger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class NopLogger extends AbstractLogger {

    public static final Logger INSTANCE = new NopLogger();

    private NopLogger() {
    }

    protected void doLog(Level level, Throwable throwable, String message, Object... args) {
    }

    public boolean canLog(Level level) {
        return false;
    }
}
