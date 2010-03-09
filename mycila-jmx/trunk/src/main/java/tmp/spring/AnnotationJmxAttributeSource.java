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

import tmp.spring.annot.ManagedOperationParameters;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Implementation of the <code>JmxAttributeSource</code> interface that
 * reads JDK 1.5+ annotations and exposes the corresponding attributes.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Jennifer Hickey
 * @see tmp.spring.annot.ManagedResource
 * @see tmp.spring.annot.ManagedAttribute
 * @see tmp.spring.annot.ManagedOperation
 * @since 1.2
 */
public class AnnotationJmxAttributeSource implements JmxAttributeSource {

    public ManagedResource getManagedResource(Class<?> beanClass) throws InvalidMetadataException {
        tmp.spring.annot.ManagedResource ann =
                beanClass.getAnnotation(tmp.spring.annot.ManagedResource.class);
        if (ann == null) {
            return null;
        }
        ManagedResource managedResource = new ManagedResource();
        //TODO AnnotationBeanUtils.copyPropertiesToBean(ann, managedResource);
        if (!"".equals(ann.value()) && !StringUtils.hasLength(managedResource.getObjectName())) {
            managedResource.setObjectName(ann.value());
        }
        return managedResource;
    }

    public ManagedAttribute getManagedAttribute(Method method) throws InvalidMetadataException {
        tmp.spring.annot.ManagedAttribute ann =
                AnnotationUtils.findAnnotation(method, tmp.spring.annot.ManagedAttribute.class);
        if (ann == null) {
            return null;
        }
        ManagedAttribute managedAttribute = new ManagedAttribute();
        //TODO AnnotationBeanUtils.copyPropertiesToBean(ann, managedAttribute, "defaultValue");
        return managedAttribute;
    }

    public ManagedMetric getManagedMetric(Method method) throws InvalidMetadataException {
        tmp.spring.annot.ManagedMetric ann =
                AnnotationUtils.findAnnotation(method, tmp.spring.annot.ManagedMetric.class);
        if (ann == null) {
            return null;
        }
        ManagedMetric managedMetric = new ManagedMetric();
        //TODO AnnotationBeanUtils.copyPropertiesToBean(ann, managedMetric);
        return managedMetric;
    }

    public ManagedOperation getManagedOperation(Method method) throws InvalidMetadataException {
        Annotation ann = AnnotationUtils.findAnnotation(method, tmp.spring.annot.ManagedOperation.class);
        if (ann == null) {
            return null;
        }
        ManagedOperation op = new ManagedOperation();
        //TODO AnnotationBeanUtils.copyPropertiesToBean(ann, op);
        return op;
    }

    public ManagedOperationParameter[] getManagedOperationParameters(Method method)
            throws InvalidMetadataException {

        ManagedOperationParameters params = AnnotationUtils.findAnnotation(method, ManagedOperationParameters.class);
        ManagedOperationParameter[] result = null;
        if (params == null) {
            result = new ManagedOperationParameter[0];
        } else {
            Annotation[] paramData = params.value();
            result = new ManagedOperationParameter[paramData.length];
            for (int i = 0; i < paramData.length; i++) {
                Annotation annotation = paramData[i];
                ManagedOperationParameter managedOperationParameter = new ManagedOperationParameter();
                //TODO AnnotationBeanUtils.copyPropertiesToBean(annotation, managedOperationParameter);
                result[i] = managedOperationParameter;
            }
        }
        return result;
    }

}