/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.testing.plugins.jetty.locator;

import static com.mycila.testing.plugins.jetty.locator.StrategyFileLocator.Strategy.findStrategy;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.annotation.ExpectException;
import com.mycila.testing.plugins.jetty.locator.AntFileLocator;
import com.mycila.testing.plugins.jetty.locator.PathFileLocator;
import com.mycila.testing.plugins.jetty.locator.RegFileLocator;
import com.mycila.testing.plugins.jetty.locator.StrategyFileLocator;
import com.mycila.testing.plugins.jetty.locator.SysFileLocator;
import com.mycila.testing.plugins.jetty.locator.StrategyFileLocator.Strategy;

/**
 * Unit test of {@link StrategyFileLocator}.
 */
@RunWith(MycilaJunitRunner.class)
public class StrategyFileLocatorTest {

    @Test
    public final void testFindStrategy()
    {
        assertThat(findStrategy("").getLocator(), instanceOf(PathFileLocator.class));
        assertThat(findStrategy("abc:").getLocator(), instanceOf(PathFileLocator.class));
        assertThat(findStrategy(":").getLocator(), instanceOf(PathFileLocator.class));

        assertThat(findStrategy("reg:").getLocator(), instanceOf(RegFileLocator.class));
        assertThat(findStrategy("reg:path").getLocator(), instanceOf(RegFileLocator.class));

        assertThat(findStrategy("ant:path").getLocator(), instanceOf(AntFileLocator.class));
        assertThat(findStrategy("ant:").getLocator(), instanceOf(AntFileLocator.class));

        assertThat(findStrategy("sys:").getLocator(), instanceOf(SysFileLocator.class));
        assertThat(findStrategy("sys:path").getLocator(), instanceOf(SysFileLocator.class));
    }


    @Test
    public final void testStrategyMatches_Default_Null()
    {
        assertFalse(Strategy.DEFAULT.matches(null));
    }


    @Test
    public final void testStrategyMatches_Default_Empty()
    {
        assertTrue(Strategy.DEFAULT.matches(""));
    }


    @Test
    public final void testStrategyMatches_Default_Path()
    {
        assertTrue(Strategy.DEFAULT.matches("path"));
    }


    @Test
    public final void testStrategyMatches_Reg_Null()
    {
        assertFalse(Strategy.REG.matches(null));
    }


    @Test
    public final void testStrategyMatches_Reg_Empty()
    {
        assertFalse(Strategy.REG.matches(""));
    }


    @Test
    public final void testStrategyMatches_Reg_Path()
    {
        assertFalse(Strategy.REG.matches("path"));
    }


    @Test
    public final void testStrategyMatches_Reg_PrefixEmpty()
    {
        assertTrue(Strategy.REG.matches("reg:"));
    }


    @Test
    public final void testStrategyMatches_Reg_PrefixPath()
    {
        assertTrue(Strategy.REG.matches("reg:path"));
    }


    @Test
    public final void testStrategyMatches_Ant_Null()
    {
        assertFalse(Strategy.ANT.matches(null));
    }


    @Test
    public final void testStrategyMatches_Ant_Empty()
    {
        assertFalse(Strategy.ANT.matches(""));
    }


    @Test
    public final void testStrategyMatches_Ant_Path()
    {
        assertFalse(Strategy.ANT.matches("path"));
    }


    @Test
    public final void testStrategyMatches_Ant_PrefixEmpty()
    {
        assertTrue(Strategy.ANT.matches("ant:"));
    }


    @Test
    public final void testStrategyMatches_Ant_PrefixPath()
    {
        assertTrue(Strategy.ANT.matches("ant:path"));
    }


    @Test
    public final void testStrategyMatches_Sys_Null()
    {
        assertFalse(Strategy.SYS.matches(null));
    }


    @Test
    public final void testStrategyMatches_Sys_Empty()
    {
        assertFalse(Strategy.SYS.matches(""));
    }


    @Test
    public final void testStrategyMatches_Sys_Path()
    {
        assertFalse(Strategy.SYS.matches("path"));
    }


    @Test
    public final void testStrategyMatches_Sys_PrefixEmpty()
    {
        assertTrue(Strategy.SYS.matches("sys:"));
    }


    @Test
    public final void testStrategyMatches_Sys_PrefixPath()
    {
        assertTrue(Strategy.SYS.matches("sys:path"));
    }


    @Test
    @ExpectException(type = NullPointerException.class, message = "path should not be null")
    public final void testStrategyCleanPath_Default_Null()
    {
        Strategy.DEFAULT.cleanPath(null);
    }


    @Test
    public final void testStrategyCleanPath_Default_Empty()
    {
        assertEquals("", Strategy.DEFAULT.cleanPath(""));
    }


    @Test
    public final void testStrategyCleanPath_Default_Path()
    {
        assertEquals("path", Strategy.DEFAULT.cleanPath("path"));
    }


    @Test
    @ExpectException(type = NullPointerException.class, message = "path should not be null")
    public final void testStrategyCleanPath_Reg_Null()
    {
        Strategy.REG.cleanPath(null);
    }


    @Test
    @ExpectException(type = IllegalArgumentException.class, message = "path should starts with reg:")
    public final void testStrategyCleanPath_Reg_Empty()
    {
        Strategy.REG.cleanPath("");
    }


    @Test
    @ExpectException(type = IllegalArgumentException.class, message = "path should starts with reg:")
    public final void testStrategyCleanPath_Reg_Path()
    {
        Strategy.REG.cleanPath("path");
    }


    @Test
    public final void testStrategyCleanPath_Reg_PrefixEmpty()
    {
        assertEquals("", Strategy.REG.cleanPath("reg:"));
    }


    @Test
    public final void testStrategyCleanPath_Reg_PrefixPath()
    {
        assertEquals("path", Strategy.REG.cleanPath("reg:path"));
    }


    @Test
    @ExpectException(type = NullPointerException.class, message = "path should not be null")
    public final void testStrategyCleanPath_Ant_Null()
    {
        Strategy.ANT.cleanPath(null);
    }


    @Test
    @ExpectException(type = IllegalArgumentException.class, message = "path should starts with ant:")
    public final void testStrategyCleanPath_Ant_Empty()
    {
        Strategy.ANT.cleanPath("");
    }


    @Test
    @ExpectException(type = IllegalArgumentException.class, message = "path should starts with ant:")
    public final void testStrategyCleanPath_Ant_Path()
    {
        Strategy.ANT.cleanPath("path");
    }


    @Test
    public final void testStrategyCleanPath_Ant_PrefixEmpty()
    {
        assertEquals("", Strategy.ANT.cleanPath("ant:"));
    }


    @Test
    public final void testStrategyCleanPath_Ant_PrefixPath()
    {
        assertEquals("path", Strategy.ANT.cleanPath("ant:path"));
    }


    @Test
    @ExpectException(type = NullPointerException.class, message = "path should not be null")
    public final void testStrategyCleanPath_Sys_Null()
    {
        Strategy.SYS.cleanPath(null);
    }


    @Test
    @ExpectException(type = IllegalArgumentException.class, message = "path should starts with sys:")
    public final void testStrategyCleanPath_Sys_Empty()
    {
        Strategy.SYS.cleanPath("");
    }


    @Test
    @ExpectException(type = IllegalArgumentException.class, message = "path should starts with sys:")
    public final void testStrategyCleanPath_Sys_Path()
    {
        Strategy.SYS.cleanPath("path");
    }


    @Test
    public final void testStrategyCleanPath_Sys_PrefixEmpty()
    {
        assertEquals("", Strategy.SYS.cleanPath("sys:"));
    }


    @Test
    public final void testStrategyCleanPath_Sys_PrefixPath()
    {
        assertEquals("path", Strategy.SYS.cleanPath("sys:path"));
    }

}
