/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.testing.plugins.jetty.locator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.mycila.testing.plugins.jetty.locator.BooleanSwitchFunction;

/**
 * Unit test of {@link BooleanSwitchFunction}.
 */
public class BooleanSwitchFunctionTest {

    @Test
    public final void testApply()
    {
        final Predicate<String> predicate = mock(Predicate.class);
        final Function<String, String> trueFunction = mock(Function.class);
        final Function<String, String> falseFunction = mock(Function.class);

        when(predicate.apply("true")).thenReturn(true);
        when(predicate.apply("false")).thenReturn(false);

        final Function<String, String> function = new BooleanSwitchFunction<String, String>(
                predicate,
                trueFunction,
                falseFunction);

        function.apply("true");
        verify(trueFunction).apply("true");
        verify(falseFunction, never()).apply("true");

        function.apply("false");
        verify(trueFunction, never()).apply("false");
        verify(falseFunction).apply("false");
    }

}
