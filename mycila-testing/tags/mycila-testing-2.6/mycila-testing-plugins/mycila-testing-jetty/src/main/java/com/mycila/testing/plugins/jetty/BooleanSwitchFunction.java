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

package com.mycila.testing.plugins.jetty;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

class BooleanSwitchFunction<F, T>
        implements Function<F, T> {

    public BooleanSwitchFunction(
            final Predicate<F> predicate,
            final Function<F, T> trueFunction,
            final Function<F, T> falseFunction)
    {
        this.predicate = predicate;
        this.trueFunction = trueFunction;
        this.falseFunction = falseFunction;
    }


    @Override
    public T apply(
            final F input)
    {
        final T value = this.predicate.apply(input)
                ? this.trueFunction.apply(input)
                : this.falseFunction.apply(input);

        return value;
    }

    private final Predicate<F> predicate;

    private final Function<F, T> trueFunction;

    private final Function<F, T> falseFunction;

}
