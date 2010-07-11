/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.plugin.discovery.support;

import com.mycila.plugin.util.Assert;
import com.mycila.plugin.util.ClassUtils;
import com.mycila.plugin.util.ResourceUtils;
import com.mycila.plugin.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ResourcePatternResolver {

    private static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
    private static final String CLASSPATH_URL_PREFIX = "classpath:";

    private AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ClassLoader classLoader;
    private String[] excludePrefixes = new String[0];

    public ResourcePatternResolver() {
        this(ClassUtils.getDefaultClassLoader());
    }

    public ResourcePatternResolver(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setExcludePrefixes(String... excludePrefixes) {
        this.excludePrefixes = excludePrefixes;
    }

    public URL[] getResources(String locationPattern) throws IOException {
        Assert.notNull(locationPattern, "Location pattern must not be null");
        if (locationPattern.startsWith(CLASSPATH_ALL_URL_PREFIX)) {
            // a class path resource (multiple resources for same name possible)
            if (pathMatcher.isPattern(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()))) {
                // a class path resource pattern
                return findPathMatchingResources(locationPattern);
            } else {
                // all class path resources with the given name
                return findAllClassPathResources(locationPattern.substring(CLASSPATH_ALL_URL_PREFIX.length()));
            }
        } else {
            // Only look for a pattern after a prefix here
            // (to not get fooled by a pattern symbol in a strange prefix).
            int prefixEnd = locationPattern.indexOf(":") + 1;
            if (pathMatcher.isPattern(locationPattern.substring(prefixEnd))) {
                // a file pattern
                return findPathMatchingResources(locationPattern);
            } else {
                // a single resource with the given name
                return new URL[]{getResource(locationPattern)};
            }
        }
    }

    private URL getResource(String location) {
		if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            location = StringUtils.cleanPath(location);
            if (location.startsWith("/"))
                location = location.substring(1);
            return ClassUtils.getDefaultClassLoader().getResource(location.substring(CLASSPATH_URL_PREFIX.length()));
		}
		else {
			try {
				return new URL(location);
			}
			catch (MalformedURLException ex) {
                location = StringUtils.cleanPath(location);
                if (location.startsWith("/"))
                    location = location.substring(1);
                return ClassUtils.getDefaultClassLoader().getResource(location);
			}
		}
	}

    /**
     * Find all class location resources with the given location via the ClassLoader.
     *
     * @param location the absolute path within the classpath
     * @return the result as Resource array
     * @throws java.io.IOException in case of I/O errors
     * @see ClassLoader#getResources
     */
    private URL[] findAllClassPathResources(String location) throws IOException {
        String path = location;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        Enumeration<URL> resourceUrls = classLoader.getResources(path);
        Set<URL> result = new LinkedHashSet<URL>(16);
        while (resourceUrls.hasMoreElements()) {
            result.add(resourceUrls.nextElement());
        }
        if(path.length() == 0) {
            resourceUrls = classLoader.getResources("META-INF/MANIFEST.MF");
            while (resourceUrls.hasMoreElements()) {
                String url = resourceUrls.nextElement().toExternalForm();
                result.add(new URL(url.substring(0, url.length() - 20)));
            }
        }
        return result.toArray(new URL[result.size()]);
    }

    /**
     * Find all resources that match the given location pattern via the
     * Ant-style PathMatcher. Supports resources in jar files and zip files
     * and in the file system.
     *
     * @param locationPattern the location pattern to match
     * @return the result as Resource array
     * @throws java.io.IOException in case of I/O errors
     * @see #doFindPathMatchingJarResources
     * @see #doFindPathMatchingFileResources
     */
    private URL[] findPathMatchingResources(String locationPattern) throws IOException {
        String rootDirPath = determineRootDir(locationPattern);
        String subPattern = locationPattern.substring(rootDirPath.length());
        URL[] rootDirResources = getResources(rootDirPath);
        Set<URL> result = new LinkedHashSet<URL>(16);
        for (URL rootDirResource : rootDirResources) {
            if (ResourceUtils.isJarURL(rootDirResource)) {
                result.addAll(doFindPathMatchingJarResources(rootDirResource, subPattern));
            } else {
                result.addAll(doFindPathMatchingFileResources(rootDirResource, subPattern));
            }
        }
        return result.toArray(new URL[result.size()]);
    }

    /**
     * Determine the root directory for the given location.
     * <p>Used for determining the starting point for file matching,
     * resolving the root directory location to a <code>java.io.File</code>
     * and passing it into <code>retrieveMatchingFiles</code>, with the
     * remainder of the location as pattern.
     * <p>Will return "/WEB-INF/" for the pattern "/WEB-INF/*.xml",
     * for example.
     *
     * @param location the location to check
     * @return the part of the location that denotes the root directory
     * @see #retrieveMatchingFiles
     */
    private String determineRootDir(String location) {
        int prefixEnd = location.indexOf(":") + 1;
        int rootDirEnd = location.length();
        while (rootDirEnd > prefixEnd && pathMatcher.isPattern(location.substring(prefixEnd, rootDirEnd))) {
            rootDirEnd = location.lastIndexOf('/', rootDirEnd - 2) + 1;
        }
        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }
        return location.substring(0, rootDirEnd);
    }

    /**
     * Find all resources in jar files that match the given location pattern
     * via the Ant-style PathMatcher.
     *
     * @param rootDirResource the root directory as Resource
     * @param subPattern      the sub pattern to match (below the root directory)
     * @return the Set of matching Resource instances
     * @throws java.io.IOException in case of I/O errors
     * @see java.net.JarURLConnection
     */
    private Set<URL> doFindPathMatchingJarResources(URL rootDirResource, String subPattern) throws IOException {
        URLConnection con = rootDirResource.openConnection();
        JarFile jarFile;
        String rootEntryPath;
        boolean newJarFile = false;

        if (con instanceof JarURLConnection) {
            // Should usually be the case for traditional JAR files.
            JarURLConnection jarCon = (JarURLConnection) con;
            jarCon.setUseCaches(false);
            jarFile = jarCon.getJarFile();
            JarEntry jarEntry = jarCon.getJarEntry();
            rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
        } else {
            // No JarURLConnection -> need to resort to URL file parsing.
            // We'll assume URLs of the format "jar:path!/entry", with the protocol
            // being arbitrary as long as following the entry format.
            // We'll also handle paths with and without leading "file:" prefix.
            String urlFile = rootDirResource.getFile();
            int separatorIndex = urlFile.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
            if (separatorIndex != -1) {
                String jarFileUrl = urlFile.substring(0, separatorIndex);
                rootEntryPath = urlFile.substring(separatorIndex + ResourceUtils.JAR_URL_SEPARATOR.length());
                jarFile = getJarFile(jarFileUrl);
            } else {
                jarFile = new JarFile(urlFile);
                rootEntryPath = "";
            }
            newJarFile = true;
        }

        try {
            if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
                // Root entry path must end with slash to allow for proper matching.
                // The Sun JRE does not return a slash here, but BEA JRockit does.
                rootEntryPath = rootEntryPath + "/";
            }
            Set<URL> result = new LinkedHashSet<URL>(8);
            for (Enumeration entries = jarFile.entries(); entries.hasMoreElements();) {
                JarEntry entry = (JarEntry) entries.nextElement();
                String entryPath = entry.getName();
                boolean keep = true;
                for (String prefix : excludePrefixes) {
                    if(entryPath.startsWith(prefix)) {
                        keep = false;
                        break;
                    }
                }
                if (keep && entryPath.startsWith(rootEntryPath)) {
                    String relativePath = entryPath.substring(rootEntryPath.length());
                    if (pathMatcher.match(subPattern, relativePath)) {
                        if (relativePath.startsWith("/"))
                            relativePath = relativePath.substring(1);
                        result.add(new URL(rootDirResource, relativePath));
                    }
                }
            }
            return result;
        }
        finally {
            // Close jar file, but only if freshly obtained -
            // not from JarURLConnection, which might cache the file reference.
            if (newJarFile) {
                jarFile.close();
            }
        }
    }

    private JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
            try {
                return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
            }
            catch (URISyntaxException ex) {
                // Fallback for URLs that are not valid URIs (should hardly ever happen).
                return new JarFile(jarFileUrl.substring(ResourceUtils.FILE_URL_PREFIX.length()));
            }
        } else {
            return new JarFile(jarFileUrl);
        }
    }

    /**
     * Find all resources in the file system that match the given location pattern
     * via the Ant-style PathMatcher.
     *
     * @param rootDirResource the root directory as Resource
     * @param subPattern      the sub pattern to match (below the root directory)
     * @return the Set of matching Resource instances
     * @throws java.io.IOException in case of I/O errors
     * @see #retrieveMatchingFiles
     */
    private Set<URL> doFindPathMatchingFileResources(URL rootDirResource, String subPattern) throws IOException {
        File rootDir;
        try {
            rootDir = new File(rootDirResource.toURI());
        }
        catch (URISyntaxException ex) {
            return Collections.emptySet();
        }
        return doFindMatchingFileSystemResources(rootDir, subPattern);
    }

    /**
     * Find all resources in the file system that match the given location pattern
     * via the Ant-style PathMatcher.
     *
     * @param rootDir    the root directory in the file system
     * @param subPattern the sub pattern to match (below the root directory)
     * @return the Set of matching Resource instances
     * @throws java.io.IOException in case of I/O errors
     * @see #retrieveMatchingFiles
     */
    private Set<URL> doFindMatchingFileSystemResources(File rootDir, String subPattern) throws IOException {
        Set<File> matchingFiles = retrieveMatchingFiles(rootDir, subPattern);
        Set<URL> result = new LinkedHashSet<URL>(matchingFiles.size());
        for (File file : matchingFiles) {
            result.add(file.toURI().toURL());
        }
        return result;
    }

    /**
     * Retrieve files that match the given path pattern,
     * checking the given directory and its subdirectories.
     *
     * @param rootDir the directory to start from
     * @param pattern the pattern to match against,
     *                relative to the root directory
     * @return the Set of matching File instances
     * @throws java.io.IOException if directory contents could not be retrieved
     */
    private Set<File> retrieveMatchingFiles(File rootDir, String pattern) throws IOException {
        if (!rootDir.exists()) {
            return Collections.emptySet();
        }
        if (!rootDir.isDirectory()) {
            return Collections.emptySet();
        }
        if (!rootDir.canRead()) {
            return Collections.emptySet();
        }
        String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
        if (!pattern.startsWith("/")) {
            fullPattern += "/";
        }
        fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
        Set<File> result = new LinkedHashSet<File>(8);
        doRetrieveMatchingFiles(fullPattern, rootDir, result);
        return result;
    }

    /**
     * Recursively retrieve files that match the given pattern,
     * adding them to the given result list.
     *
     * @param fullPattern the pattern to match against,
     *                    with preprended root directory path
     * @param dir         the current directory
     * @param result      the Set of matching File instances to add to
     * @throws java.io.IOException if directory contents could not be retrieved
     */
    private void doRetrieveMatchingFiles(String fullPattern, File dir, Set<File> result) throws IOException {
        File[] dirContents = dir.listFiles();
        if (dirContents == null) return;
        for (File content : dirContents) {
            String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
            if (content.isDirectory() && pathMatcher.matchStart(fullPattern, currPath + "/")) {
                if (content.canRead())
                    doRetrieveMatchingFiles(fullPattern, content, result);
            }
            if (pathMatcher.match(fullPattern, currPath)) {
                result.add(content);
            }
        }
    }

}