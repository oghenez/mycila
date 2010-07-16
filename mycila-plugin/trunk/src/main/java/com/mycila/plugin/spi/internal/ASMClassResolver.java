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

package com.mycila.plugin.spi.internal;

import com.mycila.plugin.Loader;
import com.mycila.plugin.spi.internal.util.ClassUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ASMClassResolver implements ClassResolver {

    private final String annotationClassDesc;
    private final Loader loader;

    public ASMClassResolver(Class<? extends Annotation> annotationClass, Loader loader) {
        this.annotationClassDesc = Type.getDescriptor(annotationClass);
        this.loader = loader;
    }

    @Override
    public Loader getLoader() {
        return loader;
    }

    @Override
    public Class<?> resolve(URL url) throws ClassResolverException {
        InputStream is = null;
        try {
            is = url.openStream();
            ClassReader classReader = new ClassReader(is);
            ClassAnnotationVisitor visitor = new ClassAnnotationVisitor();
            classReader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            if ((visitor.access & Opcodes.ACC_PUBLIC) != 0 && visitor.annotations.contains(annotationClassDesc))
                return loader.loadClass(ClassUtils.convertResourcePathToClassName(visitor.name));
        } catch (IOException e) {
            throw new ClassResolverException(url, e);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException ignored) {
                }
        }
        return null;
    }

    private static final class ClassAnnotationVisitor implements ClassVisitor {

        String name;
        int access;
        List<String> annotations = new ArrayList<String>(2);

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.name = name;
            this.access = access;
        }

        @Override
        public void visitSource(String source, String debug) {
        }

        @Override
        public void visitOuterClass(String owner, String name, String desc) {
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            annotations.add(desc);
            return null;
        }

        @Override
        public void visitAttribute(Attribute attr) {
        }

        @Override
        public void visitInnerClass(String name, String outerName, String innerName, int access) {
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            return null;
        }

        @Override
        public void visitEnd() {
        }
    }
}
