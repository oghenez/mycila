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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public abstract class AbstractDefaultJettyRunWarConfig<DataType>
        implements JettyRunWarConfig<DataType> {
    
    protected URL getWarLocation(
            final String warPath)
    {
        try {
            final FileLocator fileLocator = new StrategyFileLocator();
            final File file = fileLocator.locate(warPath);
            final URL url = file.toURI().toURL();
            
            return url;
        }
        catch (final FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
    

    protected ServerLifeCycleListener getServerLifeCycleListener(
            final String serverLifeCycleListenerClassName)
    {
        try {
            final Class<ServerLifeCycleListener> clazz = (Class) Class.forName(serverLifeCycleListenerClassName);
            return this.getServerLifeCycleListener(clazz);
        }
        catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
    

    protected ServerLifeCycleListener getServerLifeCycleListener(
            final Class<? extends ServerLifeCycleListener> serverLifeCycleListenerClass)
    {
        try {
            final ServerLifeCycleListener serverLifeCycleListener = serverLifeCycleListenerClass.newInstance();
            return serverLifeCycleListener;
        }
        catch (final InstantiationException e) {
            throw new IllegalArgumentException(e);
        }
        catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
    

    @Override
    public String toString()
    {
        final ToStringHelper toString = Objects.toStringHelper(this);
        toString.add("warLocation", this.getWarLocation());
        toString.add("serverPort", Integer.toString(this.getServerPort()));
        toString.add("contextPath", this.getContextPath());
        toString.add("serverLifeCycleListenerClass", this.getServerLifeCycleListener().getClass());
        toString.add("doStartServer", Boolean.toString(this.isDoStartServer()));
        toString.add("doDeployWebapp", Boolean.toString(this.isDoDeployWebapp()));
        toString.add("skip", Boolean.toString(this.isSkip()));
        
        return toString.toString();
    }
    

    /**
     * All WAR file based on the current directory.
     */
    public static final String DEFAULT_WAR_LOCATION = "reg:.*\\.war";
    
    public static final int DEFAULT_SERVER_PORT = 9090;
    
    public static final String DEFAULT_SERVER_PORT_AS_STRING = Integer.toString(DEFAULT_SERVER_PORT);
    
    public static final String DEFAULT_CONTEXT_PATH = "/";
    
    public static final Class<? extends ServerLifeCycleListener> DEFAULT_CYCLE_LISTENER_CLASS = NopServerLifeCycleListener.class;
    
    public static final String DEFAULT_CYCLE_LISTENER_CLASS_AS_STRING = DEFAULT_CYCLE_LISTENER_CLASS.getName();
    
    public static final boolean DEFAULT_DO_START_SERVER = true;
    
    public static final String DEFAULT_DO_START_SERVER_AS_STRING = Boolean.toString(DEFAULT_DO_START_SERVER);
    
    public static final boolean DEFAULT_DO_DEPLOY_WEBAPP = true;
    
    public static final String DEFAULT_DO_DEPLOY_WEBAPP_AS_STRING = Boolean.toString(DEFAULT_DO_DEPLOY_WEBAPP);
    
    public static final boolean DEFAULT_SKIP = false;
    
    public static final String DEFAULT_SKIP_AS_STRING = Boolean.toString(DEFAULT_SKIP);
    
}
