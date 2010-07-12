/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.plugin.metadata;

import com.mycila.plugin.annotation.ActivateAfter;
import com.mycila.plugin.annotation.ActivateBefore;
import com.mycila.plugin.annotation.Export;
import com.mycila.plugin.annotation.OnStart;
import com.mycila.plugin.annotation.OnStop;
import com.mycila.plugin.annotation.Param;
import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.annotation.Scope;
import com.mycila.plugin.scope.defaults.ExpiringSingleton;
import com.mycila.plugin.scope.defaults.Singleton;

import javax.swing.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Plugin(name = "my plugin", description = "a test plugin")
@ActivateAfter(String.class)
@ActivateBefore(Integer.class)
public final class MyPlugin {

    @OnStart
    public String onStart() {
        return "start";
    }

    @OnStop
    public String onStop() {
        return "stop";
    }

    @Export
    @Scope(Singleton.class)
    public JButton button() {
        return new JButton("button");
    }

    @Export
    @Scope(value = ExpiringSingleton.class, params = @Param(name = "duration", value = "500"))
    public JLabel label() {
        return new JLabel("a label at " + System.currentTimeMillis());
    }
}
