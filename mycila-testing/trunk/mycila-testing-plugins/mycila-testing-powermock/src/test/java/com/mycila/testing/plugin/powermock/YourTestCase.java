package com.mycila.testing.plugin.powermock;

import com.mycila.testing.junit.MycilaJunitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import static org.mockito.Mockito.*;

@RunWith(MycilaJunitRunner.class)
@PrepareForTest(Static.class)
public class YourTestCase {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void testMethodThatCallsStaticMethod() {
        // mock all the static methods in a class called "Static"
        PowerMockito.mockStatic(Static.class);
        // use Mockito to set up your expectation
        when(Static.firstStaticMethod("hello")).thenReturn(0);
        when(Static.secondStaticMethod()).thenReturn(0);

        // execute your test
        Static.firstStaticMethod("hello");
        Static.firstStaticMethod("hello");
        Static.secondStaticMethod();

        // Different from Mockito, always use PowerMockito.verifyStatic() first
        PowerMockito.verifyStatic(times(2));
        // Use EasyMock-like verification semantic per static method invocation
        Static.firstStaticMethod("hello");

        // Remember to call verifyStatic() again
        PowerMockito.verifyStatic(); // default times is once
        Static.secondStaticMethod();

        // Again, remember to call verifyStatic()
        PowerMockito.verifyStatic(never());
        Static.thirdStaticMethod();
    }
}
