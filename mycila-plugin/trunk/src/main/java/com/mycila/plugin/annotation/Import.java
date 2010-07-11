package com.mycila.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method of the plugin to receive imported services.
 * The method must be public and return void.
 * The parameters of the method are used to determine which type of service to inject.
 * <p/>
 * Class hierarchy is resolved like this:
 * <p/>
 * 1. You have a class Parent and a class Child extending Parent, and a plugin exports Child.
 * <br>
 * If the method parameter is Parent, you will get the exported Child.
 * <p/>
 * 2. You have a class Parent and classes Child1 and Child2 extending Parent,
 * and Plugin1 exports Child1 and Plugin2 exports Child2,
 * and your plugin depends only on Plugin1
 * <br>
 * If the method parameter is Parent, you will get Child1 since you depend on Plugin1
 * <p/>
 * 3. You have a class Parent and classes Child1 and Child2 extending Parent,
 * and Plugin1 exports Child1 and Plugin2 exports Child2,
 * and your plugin depends on both plugins
 * <br>
 * If the method parameter is Parent, you'll get an {@link com.mycila.plugin.inject.UnresolvedImportException}.
 * You must use Child1 or Child2 to specify which import must be used since both are available by your plugin.
 * <p/>
 * Otherwise, you can also precise from which plugin the import comes from by adding annotation
 * {@link com.mycila.plugin.annotation.From} to the parameter
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Import {
}