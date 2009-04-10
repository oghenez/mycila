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
package com.mycila.log.jdk.handler;

import com.mycila.log.jdk.format.ClassFormatter;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class StderrHandler extends MycilaHandler<StreamHandler> {
    public StderrHandler() {
        super(new StreamHandler(System.err, new ClassFormatter()) {
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }

            @Override
            public void close() {
                flush();
            }
        });
    }
}
