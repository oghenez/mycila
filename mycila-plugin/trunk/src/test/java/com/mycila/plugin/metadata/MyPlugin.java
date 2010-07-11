package com.mycila.plugin.metadata;

import com.mycila.plugin.annotation.ActivateAfter;
import com.mycila.plugin.annotation.ActivateBefore;
import com.mycila.plugin.annotation.OnStart;
import com.mycila.plugin.annotation.OnStop;
import com.mycila.plugin.annotation.Plugin;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Plugin(name = "my plugin", description = "a test plugin")
@ActivateAfter(String.class)
@ActivateBefore(Integer.class)
public final class MyPlugin {

    @OnStart
    public void onStart() {
        System.out.println("start");
    }

    @OnStop
    public void onStop() {
        System.out.println("stop");
    }

}
