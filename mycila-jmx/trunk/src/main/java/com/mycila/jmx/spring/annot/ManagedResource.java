/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.jmx.spring.annot;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JDK 1.5+ class-level annotation that indicates to register instances of a
 * class with a JMX server, corresponding to the ManagedResource attribute.
 * <p/>
 * <p><b>Note:</b> This annotation is marked as inherited, allowing for generic
 * management-aware base classes. In such a scenario, it is recommended to
 * <i>not</i> specify an object name value since this would lead to naming
 * collisions in case of multiple subclasses getting registered.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @see com.mycila.jmx.spring.export.metadata.ManagedResource
 * @since 1.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ManagedResource {

    /**
     * The annotation value is equivalent to the <code>objectName</code>
     * attribute, for simple default usage.
     */
    public abstract String value() default "";

    public abstract String objectName() default "";

    public abstract String description() default "";

    public abstract int currencyTimeLimit() default -1;

    public abstract boolean log() default false;

    public abstract String logFile() default "";

    public abstract String persistPolicy() default "";

    public abstract int persistPeriod() default -1;

    public abstract String persistName() default "";

    public abstract String persistLocation() default "";

}