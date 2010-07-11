package com.mycila.plugin.metadata;

import com.google.common.collect.Iterables;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class AnnotationMetadataBuilderTest {

    @Test
    public void test() throws Exception {
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

}
