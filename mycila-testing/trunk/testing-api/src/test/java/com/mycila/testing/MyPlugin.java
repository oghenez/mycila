package com.mycila.testing;

import com.mycila.testing.core.Context;
import com.mycila.testing.core.TestPlugin;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MyPlugin implements TestPlugin {

    public static boolean executed = false;

    public void prepareTestInstance(Context context) {
        executed = true;
    }

    public List<String> getBefore() {
        return null;
    }

    public List<String> getAfter() {
        return null;
    }
}
