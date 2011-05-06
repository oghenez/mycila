package com.mycila.testing.plugins.jetty.config;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Throwables.propagate;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.mycila.testing.plugins.jetty.JettyRunWar;
import com.mycila.testing.plugins.jetty.NopServerLifeCycleListener;
import com.mycila.testing.plugins.jetty.ServerLifeCycleListener;
import com.mycila.testing.plugins.jetty.locator.FileLocator;
import com.mycila.testing.plugins.jetty.locator.StrategyFileLocator;

public class DefaultConfig
        implements Config {

    public DefaultConfig()
    {
    }


    protected DefaultConfig(
            final Properties config)
    {
    }


    protected DefaultConfig(
            final RawConfig config)
    {
        this.setWarLocation(config.getWarLocation());
        this.setContextPath(config.getContextPath());
        this.setServerPort(config.getServerPort());
        this.setStartServer(config.isStartServer());
        this.setDeployWebapp(config.isDeployWebapp());
        this.setSkip(config.isSkip());
        this.setServerLifeCycleListenerClass(config.getServerLifeCycleListenerClass());
    }


    public URL getWarLocationUrl()
    {
        try {
            final FileLocator fileLocator = new StrategyFileLocator();
            final File file = fileLocator.locate(this.warLocation);
            final URL url = file.toURI().toURL();

            return url;
        }
        catch (final FileNotFoundException e) {
            throw propagate(e);
        }
        catch (final MalformedURLException e) {
            throw propagate(e);
        }
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.config.RawConfig#getWarLocation()
     */
    public String getWarLocation()
    {
        return this.warLocation;
    }


    void setWarLocation(
            final String warLocation)
    {
        this.warLocation = warLocation;
    }


    boolean isDefaultWarLocation()
    {
        return DEFAULT_WAR_LOCATION.equals(this.warLocation);
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.config.RawConfig#getServerPort()
     */
    public int getServerPort()
    {
        return this.serverPort;
    }


    void setServerPort(
            final int serverPort)
    {
        this.serverPort = serverPort;
    }


    boolean isDefaultServerPort()
    {
        return DEFAULT_SERVER_PORT == this.serverPort;
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.config.RawConfig#getContextPath()
     */
    public String getContextPath()
    {
        return this.contextPath;
    }


    void setContextPath(
            final String contextPath)
    {
        this.contextPath = contextPath;
    }


    boolean isDefaultContextPath()
    {
        return DEFAULT_CONTEXT_PATH.equals(this.contextPath);
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.config.RawConfig#isStartServer()
     */
    public boolean isStartServer()
    {
        return this.startServer;
    }


    void setStartServer(
            final boolean startServer)
    {
        this.startServer = startServer;
    }


    boolean isDefaultStartServer()
    {
        return DEFAULT_START_SERVER == this.startServer;
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.config.RawConfig#isDeployWebapp()
     */
    public boolean isDeployWebapp()
    {
        return this.deployWebapp;
    }


    void setDeployWebapp(
            final boolean deployWebapp)
    {
        this.deployWebapp = deployWebapp;
    }


    boolean isDefaultDeployWebapp()
    {
        return DEFAULT_DEPLOY_WEBAPP == this.deployWebapp;
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.config.RawConfig#isSkip()
     */
    public boolean isSkip()
    {
        return this.skip;
    }


    void setSkip(
            final boolean skip)
    {
        this.skip = skip;
    }


    boolean isDefaultSkip()
    {
        return DEFAULT_SKIP == this.skip;
    }


    public ServerLifeCycleListener getServerLifeCycleListener()
    {
        try {
            return this.serverLifeCycleListenerClass.newInstance();
        }
        catch (final InstantiationException e) {
            throw propagate(e);
        }
        catch (final IllegalAccessException e) {
            throw propagate(e);
        }
    }


    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.config.RawConfig#getServerLifeCycleListenerClass()
     */
    public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
    {
        return this.serverLifeCycleListenerClass;
    }


    void setServerLifeCycleListenerClass(
            final Class<? extends ServerLifeCycleListener> serverLifeCycleListenerClass)
    {
        this.serverLifeCycleListenerClass = serverLifeCycleListenerClass;
    }


    boolean isDefaultServerLifeCycleListenerClass()
    {
        return DEFAULT_CYCLE_LISTENER_CLASS.equals(this.serverLifeCycleListenerClass);
    }


    public Class<?> getSourceClass()
    {
        return this.sourceClass;
    }


    void setSourceClass(
            final Class<?> sourceClass)
    {
        this.sourceClass = sourceClass;
    }


    public Method getSourceMethod()
    {
        return this.sourceMethod;
    }


    void setSourceMethod(
            final Method sourceMethod)
    {
        this.sourceMethod = sourceMethod;
    }


    public JettyRunWar getSourceConfig()
    {
        return this.sourceConfig;
    }


    void setSourceConfig(
            final JettyRunWar sourceConfig)
    {
        this.sourceConfig = sourceConfig;
    }


    @Override
    public String toString()
    {
        final ToStringHelper toString = Objects.toStringHelper(this);
        toString.add("warLocation", this.getWarLocation());
        toString.add("serverPort", Integer.toString(this.getServerPort()));
        toString.add("contextPath", this.getContextPath());
        toString.add("serverLifeCycleListenerClass", this.getServerLifeCycleListenerClass());
        toString.add("doStartServer", Boolean.toString(this.isStartServer()));
        toString.add("doDeployWebapp", Boolean.toString(this.isDeployWebapp()));
        toString.add("skip", Boolean.toString(this.isSkip()));

        return toString.toString();
    }


    public static boolean hasJettyPlugin(
            final Method method)
    {
        final boolean hasMethodAnno = method.isAnnotationPresent(JettyRunWar.class);
        final boolean hasClassAnno = hasJettyPlugin(method.getDeclaringClass());
        final boolean hasPlugin = (hasMethodAnno && hasClassAnno) || hasClassAnno;

        return hasPlugin;
    }


    public static boolean hasJettyPlugin(
            final Class<?> klass)
    {
        final boolean hasClassAnno = klass.isAnnotationPresent(JettyRunWar.class);

        return hasClassAnno;
    }


    public static DefaultConfig configFrom(
            final Method method)
    {
        if (!hasJettyPlugin(method)) {
            throw new IllegalArgumentException("at least " + method.getDeclaringClass() + " should be annotated with "
                    + JettyRunWar.class);
        }

        final JettyRunWar methodAnno = method.getAnnotation(JettyRunWar.class);
        final boolean isMethodDefaultConfig = DefaultConfig.DEFAULT_CONFIG_CLASS.equals((methodAnno == null)
                ? null
                : methodAnno.value());

        final JettyRunWar classAnno = method.getDeclaringClass().getAnnotation(JettyRunWar.class);
        final boolean isClassDefaultConfig = DefaultConfig.DEFAULT_CONFIG_CLASS.equals((classAnno == null)
                ? null
                : classAnno.value());

        if (isClassDefaultConfig && isMethodDefaultConfig) {
            throw new IllegalArgumentException(method + " should not be annotated with " + JettyRunWar.class
                    + " because it has the same configuration has its class " + method.getDeclaringClass());
        }
        if (!isClassDefaultConfig && isMethodDefaultConfig) {
            throw new IllegalArgumentException(method + " must be annotated with " + JettyRunWar.class
                    + " and has a custom configuration because its class " + method.getDeclaringClass()
                    + " has a custom configuration");
        }

        final JettyRunWar sourceConfig = firstNonNull(methodAnno, classAnno);

        return configFrom(sourceConfig, method, method.getDeclaringClass());
    }


    public static DefaultConfig configFrom(
            final Class<?> klass)
    {
        if (!hasJettyPlugin(klass)) {
            throw new IllegalArgumentException("at least " + klass + " should be annotated with " + JettyRunWar.class);
        }

        final JettyRunWar classAnno = klass.getAnnotation(JettyRunWar.class);

        return configFrom(classAnno, null, klass);
    }


    private static DefaultConfig configFrom(
            final JettyRunWar sourceConfig,
            final Method method,
            final Class<?> klass)
    {
        try {
            final RawConfig rawConfig = sourceConfig.value().newInstance();

            final DefaultConfig config = new DefaultConfig(rawConfig);
            config.setSourceConfig(sourceConfig);
            config.setSourceMethod(method);
            config.setSourceClass(klass);

            return config;
        }
        catch (final InstantiationException e) {
            throw propagate(e);
        }
        catch (final IllegalAccessException e) {
            throw propagate(e);
        }
    }

    public static final Class<? extends RawConfig> DEFAULT_CONFIG_CLASS = DefaultConfig.class;

    public static final String DEFAULT_WAR_LOCATION = "reg:.*\\.war";

    public static final int DEFAULT_SERVER_PORT = 9090;

    public static final String DEFAULT_SERVER_PORT_AS_STRING = Integer.toString(DEFAULT_SERVER_PORT);

    /**
     * ITS (integration tests)
     */
    public static final String DEFAULT_CONTEXT_PATH = "/its";

    public static final boolean DEFAULT_START_SERVER = true;

    public static final String DEFAULT_START_SERVER_AS_STRING = Boolean.toString(DEFAULT_START_SERVER);

    public static final boolean DEFAULT_DEPLOY_WEBAPP = true;

    public static final String DEFAULT_DEPLOY_WEBAPP_AS_STRING = Boolean.toString(DEFAULT_DEPLOY_WEBAPP);

    public static final boolean DEFAULT_SKIP = false;

    public static final Class<? extends ServerLifeCycleListener> DEFAULT_CYCLE_LISTENER_CLASS = NopServerLifeCycleListener.class;

    public static final String DEFAULT_CYCLE_LISTENER_CLASS_AS_STRING = DEFAULT_CYCLE_LISTENER_CLASS.getName();

    public static final String DEFAULT_SKIP_AS_STRING = Boolean.toString(DEFAULT_SKIP);

    private String warLocation = DEFAULT_WAR_LOCATION;

    private int serverPort = DEFAULT_SERVER_PORT;

    private String contextPath = DEFAULT_CONTEXT_PATH;

    private boolean startServer = DEFAULT_START_SERVER;

    private boolean deployWebapp = DEFAULT_DEPLOY_WEBAPP;

    private boolean skip = DEFAULT_SKIP;

    private Class<? extends ServerLifeCycleListener> serverLifeCycleListenerClass = DEFAULT_CYCLE_LISTENER_CLASS;

    private Class<?> sourceClass;

    private Method sourceMethod;

    private JettyRunWar sourceConfig;
}
