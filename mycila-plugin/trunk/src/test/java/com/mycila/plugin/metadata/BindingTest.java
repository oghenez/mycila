package com.mycila.plugin.metadata;

import com.mycila.plugin.annotation.BindingAnnotation;
import com.mycila.plugin.spi.invoke.Invokables;
import com.mycila.plugin.spi.model.Binding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class BindingTest {

    @Test
    public void test() throws Exception {
        System.out.println(Binding.fromInvokable(Invokables.<Object>get(getClass().getMethod("annotated"), this)));

        System.out.println(Binding.fromParameters(getClass().getMethod("annotated")));
    }

    @Personal
    public void annotated(@Personal String param, @Personal Integer count) {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @BindingAnnotation
    static public @interface Personal {
    }

}
