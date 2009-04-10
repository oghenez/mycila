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

package com.mycila.plugin.spi;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Builder {

    String[] befores = new String[0];
    String[] afters = new String[0];

    public Builder befores(String... plugins) {
        this.befores = plugins;
        return this;
    }

    public Builder afters(String... plugins) {
        this.afters = plugins;
        return this;
    }

    public MyPlugin build() {
        return new MyAbstractPlugin() {
            public List<String> getBefore() {
                return Arrays.asList(befores);
            }

            public List<String> getAfter() {
                return Arrays.asList(afters);
            }
        };
    }

    public static Builder create() {
        return new Builder();
    }

}