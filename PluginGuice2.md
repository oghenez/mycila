**Google Guice [official website](http://code.google.com/p/google-guice/)**

The plugin works exaclty like the [Guice 1 Plugin](PluginGuice1.md), but since it uses Guice 2, you can benefits of [all improvements](http://code.google.com/p/google-guice/wiki/Changes20) such as binding overrides. So with the Guice 2 Plugin you will be able to bind your mocks directly in the runtime modules even if a binding already exist.

Example:

```
@GuiceContext(OverrideTest.MyModule.class)
public final class OverrideTest extends MycilaTestNGTest {

    @Inject
    String value;

    @Bind
    String a = "B";

    @Test
    public void test_overide() throws Exception {
        assertEquals(value, "B");
    }

    static final class MyModule extends AbstractModule {
        protected void configure() {
            bind(String.class).toInstance("A");
        }
    }
}
```