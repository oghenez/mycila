/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.testing.plugins.jetty;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test of {@link DefaultJettyRunWarConfig}.
 */
public class DefaultJettyRunWarConfigTest {

    @Test
    public final void testDefaultValues()
    {
        final DefaultJettyRunWarConfig config = new DefaultJettyRunWarConfig();
        try {
            config.getWarLocation();
            Assert.fail("must throw " + IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertThat(e.getCause(), instanceOf(FileNotFoundException.class));
        }
        Assert.assertEquals(9090, config.getServerPort());
        Assert.assertEquals("/", config.getContextPath());
        Assert.assertThat(config.getServerLifeCycleListener(), Matchers.instanceOf(NopServerLifeCycleListener.class));
        Assert.assertTrue(config.isDoStartServer());
        Assert.assertTrue(config.isDoDeployWebapp());
        Assert.assertFalse(config.isSkip());
    }

}
