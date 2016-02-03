**JMock [official website](http://www.jmock.org/)**

Here is a sample test using JMock Plugin:

```
import com.mycila.testing.core.TestSetup;
import org.jmock.Expectations;
import org.jmock.Mockery;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public final class Usage1 extends MycilaTestNGTest {

    @MockContext
    Mockery mockery;

    @Mock
    Service service;

    @Test
    public void test_go() {
        mockery.checking(new Expectations() {{
            one(service).go();
            will(returnValue("Hello world !"));
        }});
        assertEquals(service.go(), "Hello world !");
        mockery.assertIsSatisfied();
    }
}
```

**[@MockContext](http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-plugins/mycila-testing-jmock/src/main/java/com/mycila/testing/plugin/jmock/MockContext.java)** defines where you want the JMock Mockery to be injected. Thus, you could inject it more than once. This annotation is optional. If you just need to have a mock and do not need any Mockery, you can ignore it.

**[@Mock](http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-plugins/mycila-testing-jmock/src/main/java/com/mycila/testing/plugin/jmock/Mock.java)** defines a mock to be created and injected. The mock can be an interface or a class (but in the later case, you will need jmock-legacy and cglib). When the test is setting up, the JMock plugin check for all mocks to create and inject them.

**[@MockContextProvider](http://mycila.googlecode.com/svn/mycila-testing/trunk/mycila-testing-plugins/mycila-testing-jmock/src/main/java/com/mycila/testing/plugin/jmock/MockContextProvider.java)** enables you to provide the Mockery by yourself. The JMock Plugin will use it to create and inject mocks. This annotation can be put on a field, or on a method. If more than one @MockContextProvider is encoutered, the first one is choosen and the annotated method take precedenc. Here is an exemple:

```
public final class Usage2 extends MycilaTestNGTest {

    @MockContext
    Mockery mockery;

    @Mock
    Service service;

    @Test
    public void test_go() {
        mockery.checking(new Expectations() {{
            one(service).go();
            will(returnValue("Hello world !"));
        }});
        assertEquals(service.go(), "Hello world !");
        mockery.assertIsSatisfied();
    }

    @MockContextProvider
    Mockery buildMockery() {
        Mockery m = new Mockery();
        m.setImposteriser(ClassImposteriser.INSTANCE);
        m.setNamingScheme(LastWordNamingScheme.INSTANCE);
        m.setExpectationErrorTranslator(...);
        return m;
    }
}
```