# How To #

This is a [Mycila testing plugin](http://code.google.com/p/mycila/wiki/MycilaTesting) which runs your web application with [Jetty-7](http://www.eclipse.org/jetty/) before running each of your tests. you can use [selenium](http://seleniumhq.org/) to test your webapp.

Using [maven integration test](http://maven.apache.org/plugins/maven-failsafe-plugin/usage.html), your test will look like :

```
@RunWith(MycilaJunitRunner.class)
@JettyRunWar
public class MyWebAppIT {

    /**
     * Test something.
     */
    @Test
    public void testSomething()
    {
        // do and assert things with my running web application

        // you can use JettyRunWarHelper to automatically retrieve the webapp based URL
        String basedUrl = WebappHelper.getWebappUrl(this);
        Assert.assertEquals("http://localhost:9090/its", basedUrl);
    }
}
```

Maven dependency configuration to add to your pom.xml

```
<dependency>
    <groupId>com.mycila.testing.plugins</groupId>
    <artifactId>mycila-testing-jetty</artifactId>
    <version>2.8</version>
    <scope>test</scope>
</dependency>
```

_**Backward compatibility is broken since 2.8**_

# `@JettyRunWar` Configuration #

`@JettyRunWar` will find the WAR file searching from the current directory and going through sub-directories. **There must be only one matching WAR file else the test fails.**

The WAR is loaded with Jetty, running on default port 9090 and defaut contextPath /its.

So the web application is accessible at this address : http://localhost:9090/its

## Config class ##

`@JettyRunWar(MyConfig.class)` will set the web application config class.

Options :

  * WAR location
  * Server port
  * Context path
  * Start server
  * Deploy webapp
  * Skip
  * Server life cycle listener

### WAR Location ###

Will use '', 'reg:', 'sys:', 'ant:' prefix to distinguish WAR finding method.

#### Fixed ####

`warLocation : "target/webapp-1.0-SNAPSHOT.war"` will check for this WAR from the current directory.

  * matches ${currentdir}/target/webapp-1.0-SNAPSHOT.war

`WarLocation : "/tmp/webapp-1.0-SNAPSHOT.war"` will check for this WAR from the root.

#### Regular Expression  : 'reg:' ####

`WarLocation : "reg:\\.\\/target\\/webapp-.*\\.war"` will find the WAR file that matches this Java Regular Expression searching from the current directory and going through sub-directories.

  * matches ${currentdir}/target/webapp-1.0-SNAPSHOT.war
  * BUT ${currentdir}/target/web-1.0-SNAPSHOT.war
  * BUT ${currentdir}/webapp-1.0-SNAPSHOT.war

#### System Property : 'sys:' ####

`WarLocation : "sys:warPath"` will find the WAR file that matches the _warPath_ system property.

  * matches ${currentdir}/target/webapp-1.0-SNAPSHOT.war if warPath = "target/webapp-1.0-SNAPSHOT.war"

#### Ant : 'ant:' ####

[Ant Path](http://ant.apache.org/manual/dirtasks.html) are really much readable than Java Regular Expression even if there are less powerful.

`WarLocation : "ant:**/webapp-*.war"` will find the WAR file that matches this ant path searching from the current directory and going through sub-directories.

  * matches ${currentdir}/target/webapp-1.0-SNAPSHOT.war
  * BUT ${currentdir}/target/web-1.0-SNAPSHOT.war