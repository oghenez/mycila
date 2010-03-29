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

package tmp.spring;

import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Implementation of the {@link tmp.spring.export.assembler.MBeanInfoAssembler}
 * interface that reads the management interface information from source level metadata.
 * <p/>
 * <p>Uses the {@link tmp.spring.export.metadata.JmxAttributeSource} strategy interface, so that
 * metadata can be read using any supported implementation. Out of the box,
 * Spring provides an implementation based on JDK 1.5+ annotations,
 * <code>AnnotationJmxAttributeSource</code>.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Jennifer Hickey
 * @see #setAttributeSource
 * @since 1.2
 */
public class MetadataMBeanInfoAssembler extends AbstractReflectiveMBeanInfoAssembler implements AutodetectCapableMBeanInfoAssembler {

    private JmxAttributeSource attributeSource;


    /**
     * Create a new <code>MetadataMBeanInfoAssembler<code> which needs to be
     * configured through the {@link #setAttributeSource} method.
     */
    public MetadataMBeanInfoAssembler() {
    }

    /**
     * Create a new <code>MetadataMBeanInfoAssembler<code> for the given
     * <code>JmxAttributeSource</code>.
     *
     * @param attributeSource the JmxAttributeSource to use
     */
    public MetadataMBeanInfoAssembler(JmxAttributeSource attributeSource) {
        this.attributeSource = attributeSource;
    }


    /**
     * Set the <code>JmxAttributeSource</code> implementation to use for
     * reading the metadata from the bean class.
     */
    public void setAttributeSource(JmxAttributeSource attributeSource) {
        this.attributeSource = attributeSource;
    }

    public void afterPropertiesSet() {
        if (this.attributeSource == null) {
            throw new IllegalArgumentException("Property 'attributeSource' is required");
        }
    }


    /**
     * Throws an IllegalArgumentException if it encounters a JDK dynamic proxy.
     * Metadata can only be read from target classes and CGLIB proxies!
     */
    @Override
    protected void checkManagedBean(Object managedBean) throws IllegalArgumentException {
        if (AopUtils.isJdkDynamicProxy(managedBean)) {
            throw new IllegalArgumentException(
                    "MetadataMBeanInfoAssembler does not support JDK dynamic proxies - " +
                            "export the target beans directly or use CGLIB proxies instead");
        }
    }

    /**
     * Used for autodetection of beans. Checks to see if the bean's class has a
     * <code>ManagedResource</code> attribute. If so it will add it list of included beans.
     *
     * @param beanClass the class of the bean
     * @param beanName  the name of the bean in the bean factory
     */
    public boolean includeBean(Class beanClass, String beanName) {
        return (this.attributeSource.getManagedResource(getClassToExpose(beanClass)) != null);
    }

    /**
     * Vote on the inclusion of an attribute accessor.
     *
     * @param method  the accessor method
     * @param beanKey the key associated with the MBean in the beans map
     * @return whether the method has the appropriate metadata
     */
    @Override
    protected boolean includeReadAttribute(Method method, String beanKey) {
        return hasManagedAttribute(method) || hasManagedMetric(method);
    }

    /**
     * Votes on the inclusion of an attribute mutator.
     *
     * @param method  the mutator method
     * @param beanKey the key associated with the MBean in the beans map
     * @return whether the method has the appropriate metadata
     */
    @Override
    protected boolean includeWriteAttribute(Method method, String beanKey) {
        return hasManagedAttribute(method);
    }

    /**
     * Votes on the inclusion of an operation.
     *
     * @param method  the operation method
     * @param beanKey the key associated with the MBean in the beans map
     * @return whether the method has the appropriate metadata
     */
    @Override
    protected boolean includeOperation(Method method, String beanKey) {
        PropertyDescriptor pd = null;//TODO:  = BeanUtils.findPropertyForMethod(method);
        if (pd != null) {
            if (hasManagedAttribute(method)) {
                return true;
            }
        }
        return hasManagedOperation(method);
    }

    /**
     * Checks to see if the given Method has the <code>ManagedAttribute</code> attribute.
     */
    private boolean hasManagedAttribute(Method method) {
        return (this.attributeSource.getManagedAttribute(method) != null);
    }

    /**
     * Checks to see if the given Method has the <code>ManagedMetric</code> attribute.
     */
    private boolean hasManagedMetric(Method method) {
        return (this.attributeSource.getManagedMetric(method) != null);
    }

    /**
     * Checks to see if the given Method has the <code>ManagedOperation</code> attribute.
     *
     * @param method the method to check
     */
    private boolean hasManagedOperation(Method method) {
        return (this.attributeSource.getManagedOperation(method) != null);
    }


    /**
     * Reads managed resource description from the source level metadata.
     * Returns an empty <code>String</code> if no description can be found.
     */
    @Override
    protected String getDescription(Object managedBean, String beanKey) {
        ManagedResource mr = this.attributeSource.getManagedResource(getClassToExpose(managedBean));
        return (mr != null ? mr.getDescription() : "");
    }

    /**
     * Creates a description for the attribute corresponding to this property
     * descriptor. Attempts to create the description using metadata from either
     * the getter or setter attributes, otherwise uses the property name.
     */
    @Override
    protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();

        ManagedAttribute getter =
                (readMethod != null) ? this.attributeSource.getManagedAttribute(readMethod) : null;
        ManagedAttribute setter =
                (writeMethod != null) ? this.attributeSource.getManagedAttribute(writeMethod) : null;

        if (getter != null && StringUtils.hasText(getter.getDescription())) {
            return getter.getDescription();
        } else if (setter != null && StringUtils.hasText(setter.getDescription())) {
            return setter.getDescription();
        }

        ManagedMetric metric = (readMethod != null) ? this.attributeSource.getManagedMetric(readMethod) : null;
        if (metric != null && StringUtils.hasText(metric.getDescription())) {
            return metric.getDescription();
        }

        return propertyDescriptor.getDisplayName();
    }

    /**
     * Retrieves the description for the supplied <code>Method</code> from the
     * metadata. Uses the method name is no description is present in the metadata.
     */
    @Override
    protected String getOperationDescription(Method method, String beanKey) {
        PropertyDescriptor pd = null;//TODO:  = BeanUtils.findPropertyForMethod(method);
        if (pd != null) {
            ManagedAttribute ma = this.attributeSource.getManagedAttribute(method);
            if (ma != null && StringUtils.hasText(ma.getDescription())) {
                return ma.getDescription();
            }
            ManagedMetric metric = this.attributeSource.getManagedMetric(method);
            if (metric != null && StringUtils.hasText(metric.getDescription())) {
                return metric.getDescription();
            }
            return method.getName();
        } else {
            ManagedOperation mo = this.attributeSource.getManagedOperation(method);
            if (mo != null && StringUtils.hasText(mo.getDescription())) {
                return mo.getDescription();
            }
            return method.getName();
        }
    }

    /**
     * Reads <code>MBeanParameterInfo</code> from the <code>ManagedOperationParameter</code>
     * attributes attached to a method. Returns an empty array of <code>MBeanParameterInfo</code>
     * if no attributes are found.
     */
    @Override
    protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey) {
        ManagedOperationParameter[] params = this.attributeSource.getManagedOperationParameters(method);
        if (params == null || params.length == 0) {
            return new MBeanParameterInfo[0];
        }

        MBeanParameterInfo[] parameterInfo = new MBeanParameterInfo[params.length];
        Class[] methodParameters = method.getParameterTypes();

        for (int i = 0; i < params.length; i++) {
            ManagedOperationParameter param = params[i];
            parameterInfo[i] =
                    new MBeanParameterInfo(param.getName(), methodParameters[i].getName(), param.getDescription());
        }

        return parameterInfo;
    }

    /**
     * Adds descriptor fields from the <code>ManagedAttribute</code> attribute or the <code>ManagedMetric</code> attribute
     * to the attribute descriptor.
     */
    @Override
    protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey) {
        if (getter != null && hasManagedMetric(getter)) {
            populateMetricDescriptor(desc, this.attributeSource.getManagedMetric(getter));
        }
    }

    private void populateMetricDescriptor(Descriptor desc, ManagedMetric metric) {
        if (StringUtils.hasLength(metric.getDisplayName())) {
            desc.setField(FIELD_DISPLAY_NAME, metric.getDisplayName());
        }

        if (StringUtils.hasLength(metric.getUnit())) {
            desc.setField(FIELD_UNITS, metric.getUnit());
        }

        if (StringUtils.hasLength(metric.getCategory())) {
            desc.setField(FIELD_METRIC_CATEGORY, metric.getCategory());
        }

        String metricType = (metric.getMetricType() == null) ? MetricType.GAUGE.toString() : metric.getMetricType().toString();
        desc.setField(FIELD_METRIC_TYPE, metricType);
    }

}