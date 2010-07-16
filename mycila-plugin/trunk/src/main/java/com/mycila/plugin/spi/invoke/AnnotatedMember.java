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

package com.mycila.plugin.spi.invoke;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotatedMember<T extends Member & AnnotatedElement> implements Member, AnnotatedElement {

    private final T member;

    AnnotatedMember(T member) {
        this.member = member;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return member.getAnnotation(annotationClass);
    }

    @Override
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return member.isAnnotationPresent(annotationClass);
    }

    @Override
    public Class<?> getDeclaringClass() {
        return member.getDeclaringClass();
    }

    @Override
    public String getName() {
        return member.getName();
    }

    @Override
    public int getModifiers() {
        return member.getModifiers();
    }

    @Override
    public boolean isSynthetic() {
        return member.isSynthetic();
    }

    @Override
    public Annotation[] getAnnotations() {
        return member.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return member.getDeclaredAnnotations();
    }

    @Override
    public String toString() {
        return member.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotatedMember that = (AnnotatedMember) o;
        return member.equals(that.member);
    }

    @Override
    public int hashCode() {
        return member.hashCode();
    }
}
