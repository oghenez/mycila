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
        return new JButton("hello");
    }

    @Export
    @Scope(value = ExpiringSingleton.class, params = @Param(name = "duration", value = "1000"))
    public JLabel label() {
        return new JLabel("a label at " + System.currentTimeMillis());
    }
}
