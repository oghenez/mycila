package com.mycila.log.jdk;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClassFormatterTest {
    @Test
    public void test_stripped() throws Exception {
        ClassFormatter classFormatter = new ClassFormatter();
        assertEquals(classFormatter.stripped(""), "");
        assertEquals(classFormatter.stripped("AppMain"), "AppMain");
        assertEquals(classFormatter.stripped(".AppMain"), "AppMain");
        assertEquals(classFormatter.stripped("a.AppMain"), "a.AppMain");
        assertEquals(classFormatter.stripped(".a.AppMain"), "a.AppMain");
        assertEquals(classFormatter.stripped("a.b.AppMain"), "a.b.AppMain");
        assertEquals(classFormatter.stripped(".a.b.AppMain"), "a.b.AppMain");
        assertEquals(classFormatter.stripped("a.b.c.AppMain"), "b.c.AppMain");
        assertEquals(classFormatter.stripped(".a.b.c.AppMain"), "b.c.AppMain");
    }
}
