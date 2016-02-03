## Introduction ##

Mycila Testing Framework is not yet another testing framework. There are already plenty of them (Spring, Junit, TestNG, JMock, EasyMock, Mockito, JMock, DbUnit, Powermock, ...).

Mycila Testing Framework is an **integration framework**. Which means that instead of coupling the setup of your test to these frameworks, you can just setup your test with Mycila. Mycila uses a plugin infrastructure to detect which plugin should enhance your test. This way, you have only one setup to do for all your tests, whether you use jmock, easymock, guice, spring, ... Mycila also provides annotation facilities thanks to plugins, to use IOCs and Mock frameworks easier when they do not have already some annotations.

**API Version 2.0 finished !**

This new version adds a lot of improvements. The API for plugins and Custom integration has changed a lot, and thus breaks all compatibility with 1.x version.

For those only using normal test features with TestNG or Junit, you only have to change the name of the super class of your test.

Checkout the new RC1 release !

## Download ##

Versions can downloaded from Maven Repositories here:

**Releases**: http://repo2.maven.org/maven2/com/mycila/testing/

**Snapshots**: http://mc-repo.googlecode.com/svn/maven2/snapshots/com/mycila/testing/

## Maven dependencies ##

Just add these dependencies to your POM (the API and required dependencies comes transitively), with the plugins you want. See the Module page for a complete list of module.

**Example:**

```
<dependency>
    <groupId>com.mycila.testing.plugins</groupId>
    <artifactId>mycila-testing-jmock</artifactId>
    <version>X.Y</version>
</dependency>
<dependency>
    <groupId>com.mycila.testing.plugins</groupId>
    <artifactId>mycila-testing-guice</artifactId>
    <version>X.Y</version>
</dependency>
```

## Documentation ##

  * [Reports](http://old.mycila.com/p/mycila/mycila-testing/): Javadoc, Source XREF, Test reports, Coverages, ...
  * Complete list of [available modules](ModuleList.md) (API and plugins) and their documentation
  * How to [create your own plugins](PluginDevelopment.md)
  * How to [use the test enhancer in the API](TestingAPI.md)

## Samples ##

Here is an example of a unit test using Mycila Testing Framework. In this sample, we use [Google Guice](http://code.google.com/p/google-guice/) and [Mockito](http://code.google.com/p/mockito/).

```
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mycila.testing.core.TestSetup;
import com.mycila.testing.plugin.guice.GuiceContext;
import com.mycila.testing.plugin.guice.Bind;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

@GuiceContext(ClusterModule.class)
public final class ClusterNodeListenerTest extends MycilaTestNGTest {

    @Inject
    ClusterFactory factory;

    @Mock
    ClusterNodeListener listener;

    @Mock
    @Bind(scope = Singleton.class)
    ClusterService service;

    @Test
    public void test_ClusterNodeListener() throws Exception {
        ClusterState agent1 = factory.createClusterState("states");
        agent1.addListener(listener);
        ClusterState agent2 = factory.createClusterState("testatoo-states");
        ClusterState agent3 = factory.createClusterState("testatoo-states");
        Thread.sleep(4000);
        agent2.close();
        agent3.close();
        Thread.sleep(4000);
        agent1.close();
        verify(listener, times(2)).onJoin(any(UUID.class));
        verify(listener, times(2)).onLeave(any(UUID.class));
    }
}
```