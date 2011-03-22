package com.example.test;

import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.plugins.jetty.ServerLifeCycleListener;

public class ServerListener
        implements ServerLifeCycleListener {
    
    /**
     * @see com.mycila.testing.plugins.jetty.ServerLifeCycleListener#serverStarting(TestExecution, Server)
     */
    @Override
    public void serverStarting(
            final TestExecution testExecution,
            final Server server)
    {
        // init
        if (!testExecution.attributes().has("assertionsContextMap")) {
            testExecution.attributes().set("assertionsContextMap", Maps.newHashMap());
        }
        
        //ServerJettyRunWarConfigIT.class
        final String path = testExecution.context().introspector().testClass().getName() + "#"
                + testExecution.method().getName();
        this.logger.info("serverStarting : {} # {}", path, this);
        
        final Map<String, Object> assertionsContext = testExecution.attributes().get("assertionsContextMap");
        assertionsContext.put(path + "!server", server);
    }
    

    /**
     * @see com.mycila.testing.plugins.jetty.ServerLifeCycleListener#serverStarted(TestExecution, Server)
     */
    @Override
    public void serverStarted(
            final TestExecution testExecution,
            final Server server)
    {
        final String path = testExecution.context().introspector().testClass().getName() + "#"
                + testExecution.method().getName();
        this.logger.info("serverStarted : {} # {}", path, this);
    }
    

    /**
     * @see com.mycila.testing.plugins.jetty.ServerLifeCycleListener#serverStopping(TestExecution, Server)
     */
    @Override
    public void serverStopping(
            final TestExecution testExecution,
            final Server server)
    {
        final String path = testExecution.context().introspector().testClass().getName() + "#"
                + testExecution.method().getName();
        this.logger.info("serverStopping : {} # {}", path, this);
    }
    

    /**
     * @see com.mycila.testing.plugins.jetty.ServerLifeCycleListener#serverStopped(TestExecution, Server)
     */
    @Override
    public void serverStopped(
            final TestExecution testExecution,
            final Server server)
    {
        final String path = testExecution.context().introspector().testClass().getName() + "#"
                + testExecution.method().getName();
        this.logger.info("serverStopped : {} # {}", path, this);
    }
    

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
}
