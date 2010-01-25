package com.mycila.ujd;

import com.google.common.base.Predicates;
import com.mycila.ujd.api.ContainedClass;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.JVMUpdater;
import com.mycila.ujd.api.JavaClassContainedLoaded;
import com.mycila.ujd.api.LoadedClass;
import com.mycila.ujd.api.Loader;
import com.mycila.ujd.impl.DefaultJVMUpdater;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class UjdTest {
    @Test
    public void test() throws Exception {

        URLClassLoader cl = new URLClassLoader(
                new URL[]{
                        new URL(new URL("http://repo2.maven.org/maven2/com/mycila/mycila-log/2.9/mycila-log-2.9.jar"), "jar:http://repo2.maven.org/maven2/com/mycila/mycila-log/2.9/mycila-log-2.9.jar!/"),
                        new URL("http://repo2.maven.org/maven2/com/mycila/mycila-log/2.9/mycila-log-2.9.jar"),
                        new URL("file:/C:/data/workspace/PERSO/mycila-ujd/target/classes/")},
                ClassLoader.getSystemClassLoader().getParent());
        //Class<?> remote1 = cl.loadClass("com.mycila.log.Level");
        Class<?> remote2 = cl.loadClass("com.mycila.ujd.api.Loader");

        JVMUpdater updater = new DefaultJVMUpdater();
        updater.addClasses(remote2);
        for (LoadedClass loadedClass : updater.get().getLoadedClasses()) {
            System.out.println(loadedClass);
            for (Container container : loadedClass.getLoader().getContainers())
                for (ContainedClass aClass : container.getClasses())
                    System.out.println(aClass);
        }

        updater.addClasses(UjdTest.class, Loader.class, Predicates.class);
        for (JavaClassContainedLoaded<?> loadedClass : updater.get().<JavaClassContainedLoaded<?>>getClasses(Predicates.instanceOf(JavaClassContainedLoaded.class))) {
            System.out.println(loadedClass);
            System.out.println(loadedClass.getLoader());
            System.out.println(loadedClass.getContainer());
            for (ContainedClass containedClass : loadedClass.getContainer().getClasses())
                System.out.println(containedClass);
        }

        /*for (LoadedClass loadedClass : updater.get().getLoadedClasses()) {
            System.out.println(loadedClass);
            System.out.println(loadedClass.getLoader());
            System.out.println(loadedClass.getLoader().getParent());
            System.out.println(loadedClass.getLoader().getParent().getParent());
            System.out.println("===");
            for (Container container : loadedClass.getLoader().getContainers())
                System.out.println(container);
            System.out.println("===");
            for (Container container : loadedClass.getLoader().getParent().getContainers())
                System.out.println(container);
            System.out.println("===");
            for (Loader loader : loadedClass.getLoader().getChilds())
                System.out.println("child: " + loader);
        }

        for (ContainedClass aClass : updater.get().getContainedClasses()) {
            System.out.println(aClass);
        }*/
    }
}
