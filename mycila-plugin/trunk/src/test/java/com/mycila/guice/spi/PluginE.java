/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
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

package com.mycila.guice.spi;

import com.mycila.guice.annotation.ActivateAfter;
import com.mycila.guice.annotation.OnClose;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@ActivateAfter(PluginA.class)
final class PluginE {
    @OnClose
    void start(int a) {
        Collector.add("E");
    }
}