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

import com.mycila.plugin.annotation.*;
import com.mycila.plugin.scope.annotation.ExpiringSingleton;
import com.mycila.plugin.scope.annotation.Singleton;

import javax.swing.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Plugin(name = "my plugin", description = "a test plugin")
@ActivateAfter(String.class)
@ActivateBefore(Integer.class)
public final class MyPlugin {

    @Import
    private String val;

    @OnStart
    public String onStart() {
        return "start";
    }

    @OnStop
    public String onStop() {
        return "stop";
    }

    @Export
    @Singleton
    public JButton button() {
        return new JButton("button");
    }

    @Export
    @ExpiringSingleton(500)
    public JLabel label() {
        return new JLabel("a label at " + System.currentTimeMillis());
    }

    @Import
    public void inject1(@From(JButton.class) String a, Integer b) {

    }

    @Import
    public void inject1(Byte b) {

    }
}
