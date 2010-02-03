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

import com.mycila.jmx.spring.support.MetricType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JDK 1.5+ method-level annotation that indicates to expose a given bean
 * property as JMX attribute, with added Descriptor properties to indicate that
 * it is a metric. Only valid when used on a JavaBean getter.
 *
 * @author Jennifer Hickey
 * @see com.mycila.jmx.spring.export.metadata.ManagedMetric
 * @since 3.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManagedMetric {

    public abstract String category() default "";

    public abstract int currencyTimeLimit() default -1;

    public abstract String description() default "";

    public abstract String displayName() default "";

    public abstract MetricType metricType() default MetricType.GAUGE;

    public abstract int persistPeriod() default -1;

    public abstract String persistPolicy() default "";

    public abstract String unit() default "";

}