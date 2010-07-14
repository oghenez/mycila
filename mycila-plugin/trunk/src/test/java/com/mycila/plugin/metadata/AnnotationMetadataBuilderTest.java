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

package com.mycila.plugin.metadata;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.mycila.plugin.metadata.model.InexistingExportException;
import com.mycila.plugin.metadata.model.InjectionPoint;
import com.mycila.plugin.metadata.model.NoUniqueExportException;
import com.mycila.plugin.metadata.model.PluginExport;
import com.mycila.plugin.metadata.model.PluginImport;
import com.mycila.plugin.metadata.model.PluginMetadata;
import com.mycila.plugin.scope.annotation.ExpiringSingleton;
import com.mycila.plugin.scope.annotation.Singleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class AnnotationMetadataBuilderTest {

    @Test
    public void test_metadata() throws Exception {
        AnnotationMetadataBuilder builder = new AnnotationMetadataBuilder();
        PluginMetadata metadata = builder.getMetadata(new MyPlugin());

        assertEquals("my plugin", metadata.getName());
        assertEquals("a test plugin", metadata.getDescription());
        assertEquals(MyPlugin.class, metadata.getType());

        assertEquals(1, Iterables.size(metadata.getActivateBefore()));
        assertEquals(Integer.class, Iterables.getOnlyElement(metadata.getActivateBefore()));

        assertEquals(1, Iterables.size(metadata.getActivateAfter()));
        assertEquals(String.class, Iterables.getOnlyElement(metadata.getActivateAfter()));

        assertEquals("start", metadata.onStart().invoke());
        assertEquals("stop", metadata.onStop().invoke());
    }

    @Test
    public void test_exports() throws Exception {
        AnnotationMetadataBuilder builder = new AnnotationMetadataBuilder();
        PluginMetadata metadata = builder.getMetadata(new MyPlugin());

        for (PluginExport<?> export : metadata.getExports())
            System.out.println(export);

        assertEquals(2, Iterables.size(metadata.getExports()));

        assertTrue(Iterables.indexOf(metadata.getExports(), new Predicate<PluginExport<?>>() {
            @Override
            public boolean apply(PluginExport<?> input) {
                return JButton.class.equals(input.getType()) && Singleton.class.isInstance(input.getScope());
            }
        }) != -1);

        assertTrue(Iterables.indexOf(metadata.getExports(), new Predicate<PluginExport<?>>() {
            @Override
            public boolean apply(PluginExport<?> input) {
                return JLabel.class.equals(input.getType()) && ExpiringSingleton.class.isInstance(input.getScope());
            }
        }) != -1);

        assertNotNull(metadata.getExport(JButton.class));
        assertNotNull(metadata.getExport(AbstractButton.class));
        assertNotNull(metadata.getExport(JLabel.class));

        try {
            metadata.getExport(JComponent.class);
            fail();
        } catch (NoUniqueExportException e) {
            //ok
        }

        try {
            metadata.getExport(Void.class);
            fail();
        } catch (InexistingExportException e) {
            //ok
        }

        assertEquals("button", metadata.getExport(JButton.class).getProvider().get().getText());
        assertEquals("button", metadata.getExport(JButton.class).getProvider().get().getText());

        String txt = metadata.getExport(JLabel.class).getProvider().get().getText();
        assertEquals(txt, metadata.getExport(JLabel.class).getProvider().get().getText());
        System.out.println(txt);
        Thread.sleep(600);
        String txt2 = metadata.getExport(JLabel.class).getProvider().get().getText();
        System.out.println(txt2);
        assertFalse(txt.equals(txt2));
    }

    @Test
    public void test_deps() throws Exception {
        AnnotationMetadataBuilder builder = new AnnotationMetadataBuilder();
        PluginMetadata metadata = builder.getMetadata(new MyPlugin());

        assertEquals(2, Iterables.size(metadata.getInjectionPoints()));

        for (InjectionPoint point : metadata.getInjectionPoints()) {

            System.out.println(point);
            for (PluginImport pluginImport : point.getImports())
                System.out.println(" - " + pluginImport);

            if (point.getImports().size() == 1) {
                assertEquals(Byte.class, point.getImports().get(0).getType());
                assertEquals(PluginImport.FROM_ANY_PLUGIN, point.getImports().get(0).getPlugin());
            } else {
                assertEquals(String.class, point.getImports().get(0).getType());
                assertEquals(JButton.class, point.getImports().get(0).getPlugin());
                assertEquals(Integer.class, point.getImports().get(1).getType());
                assertEquals(PluginImport.FROM_ANY_PLUGIN, point.getImports().get(1).getPlugin());
            }
        }
    }
}
