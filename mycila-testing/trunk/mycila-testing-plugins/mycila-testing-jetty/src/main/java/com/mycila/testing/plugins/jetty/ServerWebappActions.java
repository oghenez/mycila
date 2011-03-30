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

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.testing.core.api.TestExecution;

public class ServerWebappActions {
    
    public void createServer(
            final TestExecution testExecution,
            final JettyRunWarConfig config)
    {
        this.setServer(new Server(config.getServerPort()));
        this.getServer().addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {
            
            @Override
            public void lifeCycleFailure(
                    final LifeCycle event,
                    final Throwable cause)
            {
                testExecution.setThrowable(cause);
            }
            

            @Override
            public void lifeCycleStarted(
                    final LifeCycle event)
            {
                ServerWebappActions.this.ready.set(true);
            }
        });
        
        final HandlerCollection handlerCollection = new HandlerCollection();
        this.setContextHandlerCollection(new ContextHandlerCollection());
        handlerCollection.addHandler(this.getContextHandlerCollection());
        this.getServer().setHandler(handlerCollection);
    }
    

    public void startServer(
            final JettyRunWarConfig config)
        throws Exception
    {
        final String serverPortStr = (config == null)
                ? "<null>"
                : Integer.toString(config.getServerPort());
        this.logger.info("server starting on localhost:{}", serverPortStr);
        if (config != null) {
            config.getServerLifeCycleListener().beforeServerStart(this.getServer());
        }
        
        this.getServer().start();
        final Callable<Boolean> isReady = new WaitUntilReadyCallable(this.ready);
        await().until(isReady, equalTo(true));
        
        if (config != null) {
            config.getServerLifeCycleListener().afterServerStart(this.getServer());
        }
        this.logger.info("server started");
    }
    

    public void stopServer(
            final JettyRunWarConfig config)
        throws Exception
    {
        this.logger.info("server stopping");
        if (config != null) {
            config.getServerLifeCycleListener().beforeServerStop(this.getServer());
        }
        
        this.getContextHandlerCollection().stop();
        this.getServer().stop();
        
        if (config != null) {
            config.getServerLifeCycleListener().afterServerStop(this.getServer());
        }
        this.logger.info("server stopped");
        
        this.getServer().destroy();
        this.setContextHandlerCollection(null);
        this.setServer(null);
    }
    

    public boolean hasServer()
    {
        return (this.getServer() != null);
    }
    

    public Server getServer()
    {
        return this.server.get();
    }
    

    public void setServer(
            final Server server)
    {
        this.server.set(server);
    }
    

    private ContextHandlerCollection getContextHandlerCollection()
    {
        return this.contextHandlerCollection.get();
    }
    

    private void setContextHandlerCollection(
            final ContextHandlerCollection contextHandlerCollection)
    {
        this.contextHandlerCollection.set(contextHandlerCollection);
    }
    

    public void createWebAppContext(
            final JettyRunWarConfig config)
        throws URISyntaxException
    {
        final File warFile = new File(config.getWarLocation().toURI());
        if (!warFile.exists()) {
            throw new AssertionError("non-existent WAR : " + warFile.getAbsolutePath());
        }
        if (!"/".equals(config.getContextPath())
                && (!config.getContextPath().startsWith("/") || config.getContextPath().endsWith("/"))) {
            throw new AssertionError("contextPath must starts with a slash '/' but doesn't end with one");
        }
        
        this.setWebAppContext(new WebAppContext());
        //webapp.addLocaleEncoding("fr_FR", "UTF-8");
        this.getWebAppContext().setWar(config.getWarLocation().getFile());
        this.getWebAppContext().setContextPath(config.getContextPath());
        this.getWebAppContext().setCopyWebDir(false);
        this.getWebAppContext().setExtractWAR(false);
        this.getWebAppContext().setLogUrlOnStart(true);
        {
            final MimeTypes mimeTypes = new MimeTypes();
            mimeTypes.addMimeMapping("js", "application/javascript");
            this.getWebAppContext().setMimeTypes(mimeTypes);
        }
        //        if (this.webdefaultFile != null) {
        //            webapp.setDefaultsDescriptor(this.webdefaultFile.getAbsolutePath());
        //        }
        
        this.logger.info("webapp on localhost:{}{} with WAR:{}", new Object[] {
                Integer.valueOf(config.getServerPort()), config.getContextPath(), config.getWarLocation()
        });
    }
    

    public void startWebApp(
            final JettyRunWarConfig config)
        throws Exception
    {
        this.logger.info("webapp starting");
        if (config != null) {
            config.getServerLifeCycleListener().beforeWebappStart(this.getServer(), this.getWebAppContext());
        }
        
        this.getContextHandlerCollection().addHandler(this.getWebAppContext());
        this.getWebAppContext().start();
        
        if (config != null) {
            config.getServerLifeCycleListener().afterWebappStart(this.getServer(), this.getWebAppContext());
        }
        this.logger.info("webapp started");
    }
    

    public void stopWebapp(
            final JettyRunWarConfig config)
        throws Exception
    {
        this.logger.info("webapp stopping");
        if (config != null) {
            config.getServerLifeCycleListener().beforeWebappStop(this.getServer(), this.getWebAppContext());
        }
        
        this.getWebAppContext().stop();
        
        if (config != null) {
            config.getServerLifeCycleListener().beforeWebappStop(this.getServer(), this.getWebAppContext());
        }
        
        this.getContextHandlerCollection().removeHandler(this.getWebAppContext());
        this.getWebAppContext().destroy();
        this.getWebAppContext().setServer(null);
        
        this.logger.info("webapp stopped");
        this.setWebAppContext(null);
    }
    

    public boolean hasWebAppContext()
    {
        return (this.getWebAppContext() != null);
    }
    

    public WebAppContext getWebAppContext()
    {
        return this.webapp.get();
    }
    

    public void setWebAppContext(
            final WebAppContext webAppContext)
    {
        this.webapp.set(webAppContext);
    }
    

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final AtomicBoolean ready = new AtomicBoolean();
    
    private final AtomicReference<Server> server = new AtomicReference<Server>();
    
    private final AtomicReference<ContextHandlerCollection> contextHandlerCollection = new AtomicReference<ContextHandlerCollection>();
    
    private final AtomicReference<WebAppContext> webapp = new AtomicReference<WebAppContext>();
    

    private static class WaitUntilReadyCallable
            implements Callable<Boolean> {
        
        public WaitUntilReadyCallable(
                final AtomicBoolean ready)
        {
            this.ready = ready;
        }
        

        public Boolean call()
            throws Exception
        {
            return this.ready.get();
        }
        

        private final AtomicBoolean ready;
        
    }
    
}
