/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.jmx.jgroups;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a public method or a field (any visibility) in
 * an MBean class defines an MBean attribute. This annotation can
 * be applied to either a field or a public setter and/or getter
 * method of a public class that is itself is optionally annotated
 * with an @MBean annotation, or inherits such an annotation from
 * a superclass.
 *
 * @author Chris Mills
 * @version $Id: ManagedAttribute.java,v 1.6 2008/03/13 02:00:23 vlada Exp $
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ManagedAttribute {
    public abstract String description() default "";

    public abstract String name() default "";

    public abstract boolean writable() default false;
}