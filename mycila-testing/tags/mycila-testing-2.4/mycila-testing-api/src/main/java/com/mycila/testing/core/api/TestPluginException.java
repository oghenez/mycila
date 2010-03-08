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
package com.mycila.testing.core.api;

import com.mycila.testing.MycilaTestingException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class TestPluginException extends MycilaTestingException {
    private static final long serialVersionUID = -7772706327921003956L;

    public TestPluginException(String message) {
        super(message);
    }

    public TestPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestPluginException(String message, Object... args) {
        super(message, args);
    }

    public TestPluginException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
