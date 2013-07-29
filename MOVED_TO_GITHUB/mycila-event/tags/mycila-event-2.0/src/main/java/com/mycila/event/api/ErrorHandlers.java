/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.event.api;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ErrorHandlers {

    private ErrorHandlers() {
    }

    public static ErrorHandler ignoreErrors() {
        return SILENT;
    }

    private static final ErrorHandler SILENT = new ErrorHandlerAdapter();

    public static ErrorHandler rethrow() {
        return new ErrorHandlerAdapter() {
            @Override
            public <E> void onError(Subscription<E> subscription, Event<E> event, Exception e) {
                Throwable t = e;
                if (e instanceof InvocationTargetException)
                    t = ((InvocationTargetException) e).getTargetException();
                if (t instanceof Error) throw (Error) t;
                if (t instanceof RuntimeException) throw (RuntimeException) t;
                DispatcherException wrapped = new DispatcherException(t.getMessage(), t);
                wrapped.setStackTrace(t.getStackTrace());
                throw wrapped;
            }
        };
    }

}
