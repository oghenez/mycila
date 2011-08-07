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

package com.mycila.testing.plugins.jetty.locator;

import static com.google.common.collect.Iterables.elementsEqual;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import com.mycila.testing.plugins.jetty.locator.AntPath;

/**
 * Unit test of {@link AntPath}.
 */
public class AntPathTest {
    
    @Test
    public void testRegex()
    {
        final String[] trueDatas = {
                "com/test.jsp",
                "com/a/test.jsp",
                "com/a/b/test.jsp",
                "com/a/b/test.jsp/c/test.jsp",
                "com/a/b/atest.jsp/c/test.jsp",
        };
        final String[] falseDatas = {
                "com/atest.jsp", "com//test.jsp", "com/a/b/atest.jsp",
        };
        
        final String[] patterns = {
                "com/([^/]+/)*test.jsp", "com/([^/]+?/)*test.jsp", "com/([^/]++/)*test.jsp",

                "com/([^/]+/)*?test.jsp", "com/([^/]+?/)*?test.jsp", "com/([^/]++/)*?test.jsp",

                "com/([^/]+/)*+test.jsp", "com/([^/]+?/)*+test.jsp", "com/([^/]++/)*+test.jsp",
        };
        
        for (final String pattern : patterns) {
            //System.out.println("for " + pattern);
            
            for (final String data : trueDatas) {
                //System.out.println("\t" + data);
                Assert.assertTrue(data.matches(pattern));
            }
        }
        for (final String pattern : patterns) {
            //System.out.println("!for " + pattern);
            
            for (final String data : falseDatas) {
                //System.out.println("\t" + data);
                Assert.assertFalse(data.matches(pattern));
            }
        }
    }
    

    @Test
    public void testSplitFunction()
    {
        assertTrue(elementsEqual(newArrayList("test"), new AntPath.SplitFunction("**").apply("test")));
        assertTrue(elementsEqual(
                newArrayList("te", "**", "st", "**", "or"),
                new AntPath.SplitFunction("**").apply("te**st**or")));
        assertTrue(elementsEqual(
                newArrayList("", "**", "te", "**", "st", "**", "or", "**", ""),
                new AntPath.SplitFunction("**").apply("**te**st**or**")));
    }
    

    @Test
    public void testMultiSplitFunction()
    {
        assertTrue(elementsEqual(newArrayList("test"), new AntPath.MultiSplitFunction("*", "**").apply("test")));
        assertTrue(elementsEqual(
                newArrayList("te", "*", "st", "**", "or"),
                new AntPath.MultiSplitFunction("**", "*").apply("te*st**or")));
        assertTrue(elementsEqual(
                newArrayList("te", "**", "st", "*", "or"),
                new AntPath.MultiSplitFunction("**", "*").apply("te**st*or")));
    }
    

    @Test
    public void testSpringAntPathMatcherAntPathZeroOrMoreCharacters()
    {
        for (final Object[] data : getAntPathZeroOrMoreCharacters()) {
            final Boolean expected = (Boolean) data[0];
            final String pattern = (String) data[1];
            final String path = (String) data[2];
            
            Assert.assertEquals(
                    "expects " + expected + " path '" + path + "' matches '" + pattern + "'",
                    expected,
                    new AntPathMatcher().match(pattern, path));
        }
    }
    

    @Test
    public void testSpringAntPathMatcherAntPathZeroOrMoreDirectories()
    {
        for (final Object[] data : getAntPathZeroOrMoreDirectories()) {
            final Boolean expected = (Boolean) data[0];
            final String pattern = (String) data[1];
            final String path = (String) data[2];
            
            Assert.assertEquals(
                    "expects " + expected + " path '" + path + "' matches '" + pattern + "'",
                    expected,
                    new AntPathMatcher().match(pattern, path));
        }
    }
    

    @Test
    public void testMatchesAntPathOneCharacter()
    {
        for (final Object[] data : getAntPathOneCharacter()) {
            final Boolean expected = (Boolean) data[0];
            final String pattern = (String) data[1];
            final String path = (String) data[2];
            
            Assert.assertEquals(
                    "expects " + expected + " path '" + path + "' matches '" + pattern + "'",
                    expected,
                    new AntPath(pattern).matches(path));
        }
    }
    

    @Test
    public void testMatchesAntPathZeroOrMoreCharacters()
    {
        for (final Object[] data : getAntPathZeroOrMoreCharacters()) {
            final Boolean expected = (Boolean) data[0];
            final String pattern = (String) data[1];
            final String path = (String) data[2];
            
            Assert.assertEquals(
                    "expects " + expected + " path '" + path + "' matches '" + pattern + "'",
                    expected,
                    new AntPath(pattern).matches(path));
        }
    }
    

    @Test
    public void testMatchesAntPathZeroOrMoreDirectories()
    {
        for (final Object[] data : getAntPathZeroOrMoreDirectories()) {
            final Boolean expected = (Boolean) data[0];
            final String pattern = (String) data[1];
            final String path = (String) data[2];
            
            Assert.assertEquals(
                    "expects " + expected + " path '" + path + "' matches '" + pattern + "'",
                    expected,
                    new AntPath(pattern).matches(path));
        }
    }
    

    private static Object[][] getAntPathOneCharacter()
    {
        //@formatter:off
        final Object[][] data = {
                {true, "com/t?st.jsp", "com/test.jsp"},
                {true, "com/t?st.jsp", "com/tast.jsp"},
                {true, "com/t?st.jsp", "com/txst.jsp"},
                {false, "com/t?st.jsp", "com/tst.jsp"},
                {false, "com/t?st.jsp", "com/teest.jsp"},
                {false, "com/t?st.jsp", "com/t/st.jsp"},
        };
        //@formatter:on
        
        return data;
    }
    

    private static Object[][] getAntPathZeroOrMoreCharacters()
    {
        //@formatter:off
        final Object[][] data = {
                // matches all .jsp files in the com directory
                {true, "com/*.jsp", "com/.jsp"},
                {true, "com/*.jsp", "com/file.jsp"},
                {true, "com/*.jsp", "com/file.data.jsp"},
                {false, "com/*.jsp", "com/path/file.jsp"},
                {false, "com/*.jsp", "comfile.jsp"},
                
                {true, "com/f*e.jsp", "com/file.jsp"},
                
                {true, "com/f*p", "com/file.jsp"},
        };
        //@formatter:on
        
        return data;
    }
    

    private static Object[][] getAntPathZeroOrMoreDirectories()
    {
        //@formatter:off
        final Object[][] data = {
                // matches all test.jsp files underneath the com path
                {true, "com/**/test.jsp", "com/test.jsp"},
                {true, "com/**/test.jsp", "com/path/test.jsp"},
                {true, "com/**/test.jsp", "com/path/to/test.jsp"},
                {false, "com/**/test.jsp", "com/atest.jsp"},
                // incompatibility between SpringAntPath and this {true, "com/**/test.jsp", "com//test.jsp"},
                {false, "com/**/test.jsp", "com/path/to/ttest.jsp"},
                
                // incompatibility between SpringAntPath and this {false, "com/***/test.jsp", "com/path/to2/test.jsp"},
                // incompatibility between SpringAntPath and this {false, "com/*******/test.jsp", "com/path/to3/test.jsp"},
                
                // matches all .jsp files underneath the org/springframework path
                {true, "org/springframework/**/*.jsp", "org/springframework/test.jsp"},
                {true, "org/springframework/**/*.jsp", "org/springframework/path/test.jsp"},
                {true, "org/springframework/**/*.jsp", "org/springframework/path/to/test.jsp"},
                {false, "org/springframework/**/*.jsp", "org.springframework/path/to/test.jsp"},
                
                // matches org/springframework/servlet/bla.jsp but also org/springframework/testing/servlet/bla.jsp and org/servlet/bla.jsp        
                {true, "org/**/servlet/bla.jsp", "org/springframework/servlet/bla.jsp"},
                {true, "org/**/servlet/bla.jsp", "org/springframework/testing/servlet/bla.jsp"},
                {true, "org/**/servlet/bla.jsp", "org/servlet/bla.jsp"},
                {false, "org/**/servlet/bla.jsp", "org.servlet/bla.jsp"},
                {false, "org/**/servlet/bla.jsp", "org/servlet/blu.jsp"},
                
                {true, "?", "t"},
                {true, "*", "test.jsp"},
                {true, "**", "com/path/to/test.jsp"},
                
                {true, "com/**", "com/"},
                {true, "com/**", "com/path/to/"},
                {true, "com/**", "com/path/to"},
                {true, "com/**", "com/test.jsp"},
                {true, "com/**", "com/path/to/test.jsp"},
                
                {true, "com/**/", "com/"},
                {true, "com/**/", "com/path/to/"},
        // incompatibility between SpringAntPath and this {true, "com/**/", "com/path/to"},
        // incompatibility between SpringAntPath and this {true, "com/**/", "com/test.jsp"},
        // incompatibility between SpringAntPath and this {true, "com/**/", "com/path/to/test.jsp"},
        };
        //@formatter:on
        
        return data;
    }
    
}
