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
