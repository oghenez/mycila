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

package com.mycila.plugin.spi;

import com.google.common.collect.Iterables;
import com.mycila.plugin.Binding;
import com.mycila.plugin.err.DuplicateExportException;
import com.mycila.plugin.err.InexistingBindingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class PluginMetadataTest {

    @Test
    public void test_metadata() throws Exception {
        PluginMetadata metadata = PluginMetadata.from(new MyPlugin());

        assertEquals("my plugin", metadata.getName());
        assertEquals("a test plugin", metadata.getDescription());
        assertEquals(MyPlugin.class, metadata.getType());

        assertEquals(1, Iterables.size(metadata.getBefores()));
        assertEquals(Integer.class, Iterables.getOnlyElement(metadata.getBefores()));

        assertEquals(1, Iterables.size(metadata.getAfters()));
        assertEquals(String.class, Iterables.getOnlyElement(metadata.getAfters()));

        assertEquals("start", metadata.onStart().invoke());
        assertEquals("stop", metadata.onStop().invoke());
    }
    
    @Test
    public void test_exports() throws Exception {
        PluginMetadata metadata = PluginMetadata.from(new MyPlugin());

        for (PluginExport<?> export : metadata.getExports())
            System.out.println(export);

        assertEquals(2, Iterables.size(metadata.getExports()));

        assertNotNull(metadata.getExport(Binding.get(JButton.class, MyPlugin.Red.class)));
        assertNotNull(metadata.getExport(Binding.get(JLabel.class, MyPlugin.Level.class, MyPlugin.Red.class)));

        try {
            metadata.getExport(Binding.get(JLabel.class));
            fail();
        } catch (InexistingBindingException e) {
            //ok
        }

        assertEquals("button", metadata.getExport(Binding.get(JButton.class, MyPlugin.Red.class)).getProvider().get().getText());
        assertEquals("button", metadata.getExport(Binding.get(JButton.class, MyPlugin.Red.class)).getProvider().get().getText());

        String txt = metadata.getExport(Binding.get(JLabel.class, MyPlugin.Level.class, MyPlugin.Red.class)).getProvider().get().getText();
        assertEquals(txt, metadata.getExport(Binding.get(JLabel.class, MyPlugin.Level.class, MyPlugin.Red.class)).getProvider().get().getText());
        System.out.println(txt);
        Thread.sleep(600);
        String txt2 = metadata.getExport(Binding.get(JLabel.class, MyPlugin.Level.class, MyPlugin.Red.class)).getProvider().get().getText();
        System.out.println(txt2);
        assertFalse(txt.equals(txt2));
    }

    @Test
    public void test_duplicate_exports() throws Exception {
        try {
            PluginMetadata.from(new DuplicatePlugin());
        } catch (Exception e) {
            assertEquals(DuplicateExportException.class, e.getClass());
        }
    }

    @Test
    public void test_imports() throws Exception {
        PluginMetadata metadata = PluginMetadata.from(new MyPlugin());

        assertEquals(6, Iterables.size(metadata.getInjectionPoints()));

        for (InjectionPoint point : metadata.getInjectionPoints()) {
            System.out.println(point);
            for (Binding<?> binding : point.getBindings())
                System.out.println(" - " + binding);
        }
    }
}
