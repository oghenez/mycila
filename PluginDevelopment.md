Creating your own plugins for Mycila Testing Framework is very easy. As an example is better than many words, let's start by a tutorial on how to write a JMock Plugin.

## The Plugin API ##

### `TestPlugin` ###

Plugins must implement [TestPlugin](http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-api/src/main/java/com/mycila/testing/core/plugin/TestPlugin.java), but **we hardly advise you extend [DefaultTestPlugin](http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-api/src/main/java/com/mycila/testing/core/plugin/DefaultTestPlugin.java)** to avoid APi breaks and to avoid implementing all methods unecessary for you.

Your plugin will be called at **each step of a test flow**:

  * prepareTestInstance
  * beforeTest
  * afterTest
  * afterClass

In these plugin methods, you have access to the current Execution context and the current Test context. The test context represents the test class and the Execution context represents the method under test / execution. The table below summary the contexts you can obtain for each method. Note that you can retrieve the Test context from the Execution context by doing `execution.context()`

| **Plugin method** | **Available contexts** |
|:------------------|:-----------------------|
| prepareTestInstance | `TestContext` context is passed as a parameter<br />`Execution execution = Mycila.currentExecution();` |
| beforeTest        | `TestExecution` context is passed as a parameter<br><code>TestExecution execution = (TestExecution) Mycila.currentExecution();</code><br><code>TestContext ctx = execution.context();</code> <br>
<tr><td> afterTest         </td><td> <code>TestExecution</code> context is passed as a parameter<br><code>TestExecution execution = (TestExecution) Mycila.currentExecution();</code><br><code>TestContext ctx = execution.context();</code> </td></tr>
<tr><td> afterClass        </td><td> <code>TestContext</code> context is passed as a parameter<br /><code>Execution execution = Mycila.currentExecution();</code> </td></tr></tbody></table>

<h3>Context</h3>

The <code>Execution</code> and sub class <code>TestExecution</code> context represents the method in test. When you develop a plugin that handles beforeTest and afterTest events, the <code>TestExecution</code> context enables you to set wheter the test method should be skipped, and enables you to get the exception thrown by the test method if any.<br>
<br>
With those 2 properties, you are able to change the behavior of the test framework and let some test pass even if they thrown an exception.<br>
<br>
A good example of this is the <a href='http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-api/src/main/java/com/mycila/testing/plugin/annotation/AnnotationTestPlugin.java'>AnnotationPlugin</a> which provides <code>@Skip</code> and <code>@ExpectException</code> annotations<br>
<br>
<h3>Introspector and Filters</h3>

The TestContext class gives you access to an Introspector on the test instance, from where you can select fields and methods through filters and compose them. In example, in Guice plugin, to select all methods providing Guice Modules and annotated by @ModuleProvider, we can call:<br>
<br>
<pre><code>import static com.mycila.testing.core.introspect.Filters.*;<br>
<br>
List&lt;Method&gt; methods = ctx.introspector().selectMethods(excludeOverridenMethods(and(methodsReturning(Module.class), methodsAnnotatedBy(ModuleProvider.class))))<br>
</code></pre>

The <code>Filters</code> class already provides common filters. The <code>Introspector</code> is in a separate package and can be used on any class instance, and you can also define your own filters. In exemple, here we want to select from <code>myObject</code> all methods annotated by @ConfigureMycilaPlugins which have only one parameter, a <code>PluginManager</code> instance.<br>
<br>
<pre><code>Introspector introspector = new Introspector(myObject);<br>
List&lt;Method&gt; methods = introspector.selectMethods(excludeOverridenMethods(and(methodsAnnotatedBy(ConfigureMycilaPlugins.class), new Filter&lt;Method&gt;() {<br>
    @Override<br>
    protected boolean accept(Method method) {<br>
        final Class&lt;?&gt;[] types = method.getParameterTypes();<br>
        return types.length == 1 &amp;&amp; types[0].equals(PluginManager.class);<br>
    }<br>
})));<br>
</code></pre>

<h3>Configure PluginManager / Test your plugin</h3>

You can use <code>@MycilaPlugins</code> and <code>@ConfigureMycilaPlugins</code> to test your plugin, as described in the <a href='TestingAPI.md'>User API wiki page</a>.<br>
<br>
Basically, <code>@MycilaPlugins</code> enables you to control which plugin descriptor to use (to isolate your plugin when testing) and this annotation enables you to configure per-test the PluginManager.<br>
<br>
If you want to define your own plugin at runtime like <a href='http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-api/src/test/java/com/mycila/testing/junit/Junit4FlowTest.java'>this example</a>, you'll have to annotate your configuration method with <code>@ConfigureMycilaPlugins</code>.<br>
<br>
<h2>Example</h2>

An example of implementation would be:<br>
<br>
<pre><code>public final class MyPlugin extends DefaultTestPlugin{<br>
    @Override<br>
    public List&lt;String&gt; getBefore() {<br>
        return null;<br>
    }<br>
    @Override<br>
    public List&lt;String&gt; getAfter() {<br>
        return null;<br>
    }<br>
    @Override<br>
    public void prepareTestInstance(Context context) {<br>
    }<br>
}<br>
</code></pre>

We don't care about receiving beforeTest, afterTest and afterClass events.<br>
<br>
<code>getBefore</code> and <code>getAfter</code> comes from <a href='http://code.google.com/p/mycila/wiki/MycilaPlugin'>Mycila Plugin Framework</a>. If provided, they help defined the order of execution of the plugins.<br>
<br>
In example, suppose we have a Spring plugin that checks for beans to include in the Application Context. If we execute the JMock Plugin before the Spring plugin, we will be able to create the Spring Application Context base on created mocks.<br>
<br>
The <a href='http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-api/src/main/java/com/mycila/testing/core/api/TestContext.java'>TestContext</a> enables the plugin to access the test instance to enhance, and also sharing data with other plugins through context attributes. A plugin can also access the plugin manager and resolver, which provides the plugin execution flow. To have more information about the plugin infrastructure, please see <a href='http://code.google.com/p/mycila/wiki/MycilaPlugin'>Mycila Plugin Framework</a> documentation.<br>
<br>
<h3>First, What do we want for our plugin ?</h3>

<ul><li>Create and inject mock<br>
</li><li>Be able to access the mockery to verify expectations</li></ul>

We would like to be able to write tests using JMock like this:<br>
<br>
<pre><code>public final class MyTest extends MycilaTestNGTest {<br>
<br>
    @MockContext<br>
    Mockery mockery;<br>
<br>
    @Mock<br>
    Service service;<br>
<br>
    @Test<br>
    public void test_go() {<br>
        mockery.checking(new Expectations() {{<br>
            // some expectations<br>
        }});<br>
        // execute test<br>
        mockery.assertIsSatisfied();<br>
    }<br>
}<br>
</code></pre>

<ul><li><b>@Mock</b> defines a mock to create and inject<br>
</li><li><b>@MockContext</b> defines a place where to inject the mockery</li></ul>

<h3>Let's code our plugin !</h3>

First, we need to have two annotations:<br>
<br>
<pre><code>@Retention(RetentionPolicy.RUNTIME)<br>
@Target({ElementType.FIELD})<br>
public @interface Mock {<br>
}<br>
<br>
@Retention(RetentionPolicy.RUNTIME)<br>
@Target({ElementType.FIELD})<br>
public @interface MockContext {<br>
}<br>
</code></pre>

Then, we prepare our plugin class:<br>
<br>
<pre><code>public final class MyPlugin extends DefaultTestPlugin {<br>
    @Override<br>
    public List&lt;String&gt; getAfter() {<br>
        return Arrays.asList("guice1", "spring");<br>
    }<br>
    @Override<br>
    public void prepareTestInstance(Context context) {<br>
        //TODO: implement<br>
    }<br>
}<br>
</code></pre>

The implementation is quite simple:<br>
<br>
<ol><li>Get the mocks to create (by listing fields annotated by @Mock)<br>
</li><li>Create a mockery<br>
</li><li>If @MockContext annotated fields are found, inject the Mockery<br>
</li><li>Create and inject mocks</li></ol>

<pre><code>Field[] mocks = context.getTest().getFieldsAnnotatedWith(Mock.class);<br>
Mockery mockery = new Mockery();<br>
for (Field field : context.introspector().selectFields(and(fieldsAccepting(Mockery.class), fieldsAnnotatedBy(MockContext.class)))) {<br>
    context.introspector().set(field, mockery);<br>
}<br>
for (Field field : mocks) {<br>
    context.introspector().set(field, mockery.mock(field.getType(), field.getDeclaringClass().getName() + "." + field.getName()));<br>
}<br>
</code></pre>

<h3>Package and distribute</h3>

Now that we have a working and testable plugin, it would be nice if it could be loaded automatically if our plugin jar is in the classpath. To detect plugins, Mycila Plugin Framework reads all property files in the classpath named:<br>
<br>
<b>META-INF/mycila/testing/plugins.properties</b>

So we just have to include a <code>plugins.properties</code> files in our jar, with the following content:<br>
<br>
<pre><code>jmock=MyPlugin<br>
</code></pre>

Et voila ! We've made with 3 classes (two annotation and a plugin class) and a property file, a JMock plugin for Mycila Testing, that can be automatically loaded and executed to enhance your tests.<br>
<br>
<h2>To go further</h2>

We highly suggest you have a look of existing plugins implementation in the <a href='http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-plugins'>source code repository</a>. There are plenty of plugins existing and you will probably find what you need on these examples.