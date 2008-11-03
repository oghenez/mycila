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

import com.mycila.plugin.spi.MyPlugin;
import com.mycila.plugin.spi.PluginManager;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SampleUse {
    public static void main(String[] args) {
        PluginManager<MyPlugin> manager = new PluginManager<MyPlugin>(MyPlugin.class, "META-INF/myapp/plugins.properties");
        for (MyPlugin plugin : manager.getResolver().getResolvedPlugins()) {
            plugin.execute();
        }
    }

    public static void a() {
        PluginManager<MyPlugin> manager = new PluginManager<MyPlugin>(MyPlugin.class);
        manager.getCache().registerPlugin("myPlugin1", new MyPluginInstance1());
        manager.getCache().registerPlugin("myPlugin2", new MyPluginInstance2());
        for (MyPlugin plugin : manager.getResolver().getResolvedPlugins()) {
            plugin.execute();
        }
    }

    private static class MyPluginInstance1 implements MyPlugin {
        public void execute() {
        }

        public List<String> getAfter() {
            return null;
        }

        public List<String> getBefore() {
            return null;
        }
    }

    private static class MyPluginInstance2 implements MyPlugin {
        public void execute() {
        }

        public List<String> getAfter() {
            return null;
        }

        public List<String> getBefore() {
            return null;
        }
    }
}
