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

import com.mycila.plugin.Provider;
import com.mycila.plugin.annotation.ActivateAfter;
import com.mycila.plugin.annotation.ActivateBefore;
import com.mycila.plugin.annotation.BindingAnnotation;
import com.mycila.plugin.annotation.Export;
import com.mycila.plugin.annotation.From;
import com.mycila.plugin.annotation.Import;
import com.mycila.plugin.annotation.OnStart;
import com.mycila.plugin.annotation.OnStop;
import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.annotation.scope.ExpiringSingleton;
import com.mycila.plugin.annotation.scope.Singleton;

import javax.swing.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Plugin(name = "my plugin", description = "a test plugin")
@ActivateAfter(String.class)
@ActivateBefore(Integer.class)
public final class MyPlugin<T> {

    @Import
    private String val;

    @Import
    private Provider<? extends T> val2;

    @Import
    private Provider<T> val3;

    @Import
    private Provider<Collection<Integer>> val4;

    @OnStart
    public String onStart() {
        return "start";
    }

    @OnStop
    public String onStop() {
        return "stop";
    }

    @Export
    @Red @Singleton
    public JButton button() {
        return new JButton("button");
    }

    @Export
    @ExpiringSingleton(500)
    public JLabel label() {
        return new JLabel("a label at " + System.currentTimeMillis());
    }

    @Import
    public void inject1(Provider<T> p1, Provider<? extends T> p2, Provider<Collection<Integer>> p3) {

    }

    @Import
    public void inject1(@From(JButton.class) @Red String a, @Red @Level(2) Provider<Integer> b) {

    }

    @Import
    public void inject1(Byte b) {

    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @BindingAnnotation
    static public @interface Red {
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @BindingAnnotation
    static public @interface Level {
        int value();
    }

}
