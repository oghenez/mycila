/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package tmp.spring;

import javax.management.DynamicMBean;
import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * JMX exporter that allows for exposing any <i>Spring-managed bean</i> to a
 * JMX {@link javax.management.MBeanServer}, without the need to define any
 * JMX-specific information in the bean classes.
 * <p/>
 * <p>If a bean implements one of the JMX management interfaces, MBeanExporter can
 * simply register the MBean with the server through its autodetection process.
 * <p/>
 * <p>If a bean does not implement one of the JMX management interfaces, MBeanExporter
 * will create the management information using the supplied {@link tmp.spring.export.assembler.MBeanInfoAssembler}.
 * <p/>
 * <p>A list of {@link tmp.spring.export.MBeanExporterListener MBeanExporterListeners} can be registered
 * application code to be notified of MBean registration and unregistration events.
 * <p/>
 * <p>This exporter is compatible with JMX 1.2 on Java 5 and above.
 * As of Spring 2.5, it also autodetects and exports Java 6 MXBeans.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Rick Evans
 * @author Mark Fisher
 * @see #setBeans
 * @see #setAutodetect
 * @see #setAssembler
 * @see tmp.spring.export.assembler.MBeanInfoAssembler
 * @see tmp.spring.export.MBeanExporterListener
 * @since 1.2
 */
public class MBeanExporter extends MBeanRegistrationSupport implements MBeanExportOperations {

    /**
     * Autodetection mode indicating that no autodetection should be used.
     */
    public static final int AUTODETECT_NONE = 0;

    /**
     * Autodetection mode indicating that only valid MBeans should be autodetected.
     */
    public static final int AUTODETECT_MBEAN = 1;

    /**
     * Autodetection mode indicating that only the {@link tmp.spring.export.assembler.MBeanInfoAssembler} should be able
     * to autodetect beans.
     */
    public static final int AUTODETECT_ASSEMBLER = 2;

    /**
     * Autodetection mode indicating that all autodetection mechanisms should be used.
     */
    public static final int AUTODETECT_ALL = AUTODETECT_MBEAN | AUTODETECT_ASSEMBLER;


    /**
     * Wildcard used to map a {@link javax.management.NotificationListener}
     * to all MBeans registered by the <code>MBeanExporter</code>.
     */
    private static final String WILDCARD = "*";

    /**
     * Constant for the JMX <code>mr_type</code> "ObjectReference"
     */
    private static final String MR_TYPE_OBJECT_REFERENCE = "ObjectReference";

    /**
     * Prefix for the autodetect constants defined in this class
     */
    private static final String CONSTANT_PREFIX_AUTODETECT = "AUTODETECT_";


    /**
     * The beans to be exposed as JMX managed resources, with JMX names as keys
     */
    private Map<String, Object> beans;

    /**
     * The autodetect mode to use for this MBeanExporter
     */
    private Integer autodetectMode;

    /**
     * Whether to eagerly initialize candidate beans when autodetecting MBeans
     */
    private boolean allowEagerInit = false;

    /**
     * Indicates whether Spring should modify generated ObjectNames
     */
    private boolean ensureUniqueRuntimeObjectNames = true;

    /**
     * Indicates whether Spring should expose the managed resource ClassLoader in the MBean
     */
    private boolean exposeManagedResourceClassLoader = true;

    /**
     * A set of bean names that should be excluded from autodetection
     */
    private Set<String> excludedBeans;

    /**
     * Stores the MBeanInfoAssembler to use for this exporter
     */
    private MBeanInfoAssembler assembler;

    /**
     * The strategy to use for creating ObjectNames for an object
     */
    private ObjectNamingStrategy namingStrategy;

    /**
     * Stores the ClassLoader to use for generating lazy-init proxies
     */
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();


    /**
     * Supply a <code>Map</code> of beans to be registered with the JMX
     * <code>MBeanServer</code>.
     * <p>The String keys are the basis for the creation of JMX object names.
     * By default, a JMX <code>ObjectName</code> will be created straight
     * from the given key. This can be customized through specifying a
     * custom <code>NamingStrategy</code>.
     * <p>Both bean instances and bean names are allowed as values.
     * Bean instances are typically linked in through bean references.
     * Bean names will be resolved as beans in the current factory, respecting
     * lazy-init markers (that is, not triggering initialization of such beans).
     *
     * @param beans Map with JMX names as keys and bean instances or bean names
     *              as values
     * @see #setNamingStrategy
     * @see tmp.spring.export.naming.KeyNamingStrategy
     * @see javax.management.ObjectName#ObjectName(String)
     */
    public void setBeans(Map<String, Object> beans) {
        this.beans = beans;
    }

    /**
     * Set whether to autodetect MBeans in the bean factory that this exporter
     * runs in. Will also ask an <code>AutodetectCapableMBeanInfoAssembler</code>
     * if available.
     * <p>This feature is turned off by default. Explicitly specify
     * <code>true</code> here to enable autodetection.
     *
     * @see #setAssembler
     * @see tmp.spring.export.assembler.AutodetectCapableMBeanInfoAssembler
     * @see #isMBean
     */
    public void setAutodetect(boolean autodetect) {
        this.autodetectMode = (autodetect ? AUTODETECT_ALL : AUTODETECT_NONE);
    }

    /**
     * Set the autodetection mode to use.
     *
     * @throws IllegalArgumentException if the supplied value is not
     *                                  one of the <code>AUTODETECT_</code> constants
     * @see #setAutodetectModeName(String)
     * @see #AUTODETECT_ALL
     * @see #AUTODETECT_ASSEMBLER
     * @see #AUTODETECT_MBEAN
     * @see #AUTODETECT_NONE
     */
    public void setAutodetectMode(int autodetectMode) {
        /*if (!constants.getValues(CONSTANT_PREFIX_AUTODETECT).contains(autodetectMode)) {
              throw new IllegalArgumentException("Only values of autodetect constants allowed");
          }*/
        this.autodetectMode = autodetectMode;
    }

    /**
     * Set the autodetection mode to use by name.
     *
     * @throws IllegalArgumentException if the supplied value is not resolvable
     *                                  to one of the <code>AUTODETECT_</code> constants or is <code>null</code>
     * @see #setAutodetectMode(int)
     * @see #AUTODETECT_ALL
     * @see #AUTODETECT_ASSEMBLER
     * @see #AUTODETECT_MBEAN
     * @see #AUTODETECT_NONE
     */
    public void setAutodetectModeName(String constantName) {
        if (constantName == null || !constantName.startsWith(CONSTANT_PREFIX_AUTODETECT)) {
            throw new IllegalArgumentException("Only autodetect constants allowed");
        }
        this.autodetectMode = AUTODETECT_ALL;//TODO  = (Integer) constants.asNumber(constantName);
    }

    /**
     * Specify whether to allow eager initialization of candidate beans
     * when autodetecting MBeans in the Spring application context.
     * <p>Default is "false", respecting lazy-init flags on bean definitions.
     * Switch this to "true" in order to search lazy-init beans as well,
     * including FactoryBean-produced objects that haven't been initialized yet.
     */
    public void setAllowEagerInit(boolean allowEagerInit) {
        this.allowEagerInit = allowEagerInit;
    }

    /**
     * Set the implementation of the <code>MBeanInfoAssembler</code> interface to use
     * for this exporter. Default is a <code>SimpleReflectiveMBeanInfoAssembler</code>.
     * <p>The passed-in assembler can optionally implement the
     * <code>AutodetectCapableMBeanInfoAssembler</code> interface, which enables it
     * to participate in the exporter's MBean autodetection process.
     *
     * @see tmp.spring.export.assembler.SimpleReflectiveMBeanInfoAssembler
     * @see tmp.spring.export.assembler.AutodetectCapableMBeanInfoAssembler
     * @see tmp.spring.export.assembler.MetadataMBeanInfoAssembler
     * @see #setAutodetect
     */
    public void setAssembler(MBeanInfoAssembler assembler) {
        this.assembler = assembler;
    }

    /**
     * Set the implementation of the <code>ObjectNamingStrategy</code> interface
     * to use for this exporter. Default is a <code>KeyNamingStrategy</code>.
     *
     * @see tmp.spring.export.naming.KeyNamingStrategy
     * @see tmp.spring.export.naming.MetadataNamingStrategy
     */
    public void setNamingStrategy(ObjectNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    /**
     * Set the list of names for beans that should be excluded from autodetection.
     */
    public void setExcludedBeans(String[] excludedBeans) {
        this.excludedBeans = (excludedBeans != null ? new HashSet<String>(Arrays.asList(excludedBeans)) : null);
    }

    /**
     * Indicates whether Spring should ensure that {@link javax.management.ObjectName ObjectNames}
     * generated by the configured {@link tmp.spring.export.naming.ObjectNamingStrategy} for
     * runtime-registered MBeans ({@link #registerManagedResource}) should get
     * modified: to ensure uniqueness for every instance of a managed <code>Class</code>.
     * <p>The default value is <code>true</code>.
     *
     * @see #registerManagedResource
     * @see tmp.spring.support.JmxUtils#appendIdentityToObjectName(javax.management.ObjectName, Object)
     */
    public void setEnsureUniqueRuntimeObjectNames(boolean ensureUniqueRuntimeObjectNames) {
        this.ensureUniqueRuntimeObjectNames = ensureUniqueRuntimeObjectNames;
    }

    /**
     * Indicates whether or not the managed resource should be exposed on the
     * {@link Thread#getContextClassLoader() thread context ClassLoader} before
     * allowing any invocations on the MBean to occur.
     * <p>The default value is <code>true</code>, exposing a {@link tmp.spring.export.SpringModelMBean}
     * which performs thread context ClassLoader management. Switch this flag off to
     * expose a standard JMX {@link javax.management.modelmbean.RequiredModelMBean}.
     */
    public void setExposeManagedResourceClassLoader(boolean exposeManagedResourceClassLoader) {
        this.exposeManagedResourceClassLoader = exposeManagedResourceClassLoader;
    }


    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }


    //---------------------------------------------------------------------
    // Lifecycle in bean factory: automatically register/unregister beans
    //---------------------------------------------------------------------

    /**
     * Start bean registration automatically when deployed in an
     * <code>ApplicationContext</code>.
     *
     * @see #registerBeans()
     */
    public void afterPropertiesSet() {
        // If no server was provided then try to find one. This is useful in an environment
        // such as JDK 1.5, Tomcat or JBoss where there is already an MBeanServer loaded.
        if (this.server == null) {
            this.server = JmxUtils.locateMBeanServer();
        }
        try {
            registerBeans();
        }
        catch (RuntimeException ex) {
            // Unregister beans already registered by this exporter.
            unregisterBeans();
            throw ex;
        }
    }

    /**
     * Unregisters all beans that this exported has exposed via JMX
     * when the enclosing <code>ApplicationContext</code> is destroyed.
     */
    public void destroy() {
        unregisterBeans();
    }


    //---------------------------------------------------------------------
    // Implementation of MBeanExportOperations interface
    //---------------------------------------------------------------------

    public ObjectName registerManagedResource(Object managedResource) throws MBeanExportException {
        ObjectName objectName;
        try {
            objectName = getObjectName(managedResource, null);
            if (this.ensureUniqueRuntimeObjectNames) {
                objectName = JmxUtils.appendIdentityToObjectName(objectName, managedResource);
            }
        }
        catch (Exception ex) {
            throw new MBeanExportException("Unable to generate ObjectName for MBean [" + managedResource + "]", ex);
        }
        registerManagedResource(managedResource, objectName);
        return objectName;
    }

    public void registerManagedResource(Object managedResource, ObjectName objectName) throws MBeanExportException {
        try {
            if (isMBean(managedResource.getClass())) {
                doRegister(managedResource, objectName);
            } else {
                ModelMBean mbean = createAndConfigureMBean(managedResource, managedResource.getClass().getName());
                doRegister(mbean, objectName);
            }
        }
        catch (JMException ex) {
            throw new UnableToRegisterMBeanException(
                    "Unable to register MBean [" + managedResource + "] with object name [" + objectName + "]", ex);
        }
    }

    public void unregisterManagedResource(ObjectName objectName) {
        doUnregister(objectName);
    }


    //---------------------------------------------------------------------
    // Exporter implementation
    //---------------------------------------------------------------------

    /**
     * Registers the defined beans with the {@link javax.management.MBeanServer}.
     * <p>Each bean is exposed to the <code>MBeanServer</code> via a
     * <code>ModelMBean</code>. The actual implemetation of the
     * <code>ModelMBean</code> interface used depends on the implementation of
     * the <code>ModelMBeanProvider</code> interface that is configured. By
     * default the <code>RequiredModelMBean</code> class that is supplied with
     * all JMX implementations is used.
     * <p>The management interface produced for each bean is dependent on the
     * <code>MBeanInfoAssembler</code> implementation being used. The
     * <code>ObjectName</code> given to each bean is dependent on the
     * implementation of the <code>ObjectNamingStrategy</code> interface being used.
     */
    protected void registerBeans() {
        // The beans property may be null, for example if we are relying solely on autodetection.
        if (this.beans == null) {
            this.beans = new HashMap<String, Object>();
            // Use AUTODETECT_ALL as default in no beans specified explicitly.
            if (this.autodetectMode == null) {
                this.autodetectMode = AUTODETECT_ALL;
            }
        }

        // Perform autodetection, if desired.
        int mode = (this.autodetectMode != null ? this.autodetectMode : AUTODETECT_NONE);
        if (mode != AUTODETECT_NONE) {
            /*if (this.beanFactory == null) {
                   throw new MBeanExportException("Cannot autodetect MBeans if not running in a BeanFactory");
               }*/
            if (mode == AUTODETECT_MBEAN || mode == AUTODETECT_ALL) {
                // Autodetect any beans that are already MBeans.
                autodetectMBeans();
            }
            // Allow the assembler a chance to vote for bean inclusion.
            if ((mode == AUTODETECT_ASSEMBLER || mode == AUTODETECT_ALL) &&
                    this.assembler instanceof AutodetectCapableMBeanInfoAssembler) {
                autodetectBeans((AutodetectCapableMBeanInfoAssembler) this.assembler);
            }
        }

        if (!this.beans.isEmpty()) {
            for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
                registerBeanNameOrInstance(entry.getValue(), entry.getKey());
            }
        }
    }

    /**
     * Registers an individual bean with the {@link #setServer MBeanServer}.
     * <p>This method is responsible for deciding <strong>how</strong> a bean
     * should be exposed to the <code>MBeanServer</code>. Specifically, if the
     * supplied <code>mapValue</code> is the name of a bean that is configured
     * for lazy initialization, then a proxy to the resource is registered with
     * the <code>MBeanServer</code> so that the the lazy load behavior is
     * honored. If the bean is already an MBean then it will be registered
     * directly with the <code>MBeanServer</code> without any intervention. For
     * all other beans or bean names, the resource itself is registered with
     * the <code>MBeanServer</code> directly.
     *
     * @param mapValue the value configured for this bean in the beans map;
     *                 may be either the <code>String</code> name of a bean, or the bean itself
     * @param beanKey  the key associated with this bean in the beans map
     * @return the <code>ObjectName</code> under which the resource was registered
     * @throws tmp.spring.export.MBeanExportException
     *          if the export failed
     * @see #setBeans
     * @see #registerBeanInstance
     */
    protected ObjectName registerBeanNameOrInstance(Object mapValue, String beanKey) throws MBeanExportException {
        try {
            if (mapValue instanceof String) {
                // Bean name pointing to a potentially lazy-init bean in the factory.
                /*if (this.beanFactory == null) {
                        throw new MBeanExportException("Cannot resolve bean names if not running in a BeanFactory");
                    }*/
                String beanName = (String) mapValue;
                if (true/*TODO isBeanDefinitionLazyInit(this.beanFactory, beanName)*/) {
                    ObjectName objectName = registerLazyInit(beanName, beanKey);
                    return objectName;
                } else {
                    Object bean = null;//TODO this.beanFactory.getBean(beanName);
                    ObjectName objectName = registerBeanInstance(bean, beanKey);
                    return objectName;
                }
            } else {
                // Plain bean instance -> register it directly.
                if (true/*TODO this.beanFactory != null*/) {
                    Map<String, ?> beansOfSameType = null;
                    //this.beanFactory.getBeansOfType(mapValue.getClass(), false, this.allowEagerInit);
                    for (Map.Entry<String, ?> entry : beansOfSameType.entrySet()) {
                        if (entry.getValue() == mapValue) {
                            String beanName = entry.getKey();
                            ObjectName objectName = registerBeanInstance(mapValue, beanKey);
                            return objectName;
                        }
                    }
                }
                return registerBeanInstance(mapValue, beanKey);
            }
        }
        catch (Exception ex) {
            throw new UnableToRegisterMBeanException(
                    "Unable to register MBean [" + mapValue + "] with key '" + beanKey + "'", ex);
        }
    }

    /**
     * Registers an existing MBean or an MBean adapter for a plain bean
     * with the <code>MBeanServer</code>.
     *
     * @param bean    the bean to register, either an MBean or a plain bean
     * @param beanKey the key associated with this bean in the beans map
     * @return the <code>ObjectName</code> under which the bean was registered
     *         with the <code>MBeanServer</code>
     */
    private ObjectName registerBeanInstance(Object bean, String beanKey) throws JMException {
        ObjectName objectName = getObjectName(bean, beanKey);
        Object mbeanToExpose = null;
        if (isMBean(bean.getClass())) {
            mbeanToExpose = bean;
        } else {
            DynamicMBean adaptedBean = adaptMBeanIfPossible(bean);
            if (adaptedBean != null) {
                mbeanToExpose = adaptedBean;
            }
        }
        if (mbeanToExpose != null) {
            doRegister(mbeanToExpose, objectName);
        } else {
            ModelMBean mbean = createAndConfigureMBean(bean, beanKey);
            doRegister(mbean, objectName);
        }
        return objectName;
    }

    /**
     * Registers beans that are configured for lazy initialization with the
     * <code>MBeanServer<code> indirectly through a proxy.
     *
     * @param beanName the name of the bean in the <code>BeanFactory</code>
     * @param beanKey  the key associated with this bean in the beans map
     * @return the <code>ObjectName</code> under which the bean was registered
     *         with the <code>MBeanServer</code>
     */
    private ObjectName registerLazyInit(String beanName, String beanKey) throws JMException {
        /*ProxyFactory proxyFactory = new ProxyFactory();
          proxyFactory.setProxyTargetClass(true);
          proxyFactory.setFrozen(true);*/

        /*if (isMBean(this.beanFactory.getType(beanName))) {
              // A straight MBean... Let's create a simple lazy-init CGLIB proxy for it.
              LazyInitTargetSource targetSource = new LazyInitTargetSource();
              targetSource.setTargetBeanName(beanName);
              targetSource.setBeanFactory(this.beanFactory);
              proxyFactory.setTargetSource(targetSource);

              Object proxy = proxyFactory.getProxy(this.beanClassLoader);
              ObjectName objectName = getObjectName(proxy, beanKey);
              if (logger.isDebugEnabled()) {
                  logger.debug("Located MBean '" + beanKey + "': registering with JMX server as lazy-init MBean [" +
                          objectName + "]");
              }
              doRegister(proxy, objectName);
              return objectName;
          }

          else {
              // A simple bean... Let's create a lazy-init ModelMBean proxy with notification support.
              NotificationPublisherAwareLazyTargetSource targetSource = new NotificationPublisherAwareLazyTargetSource();
              targetSource.setTargetBeanName(beanName);
              targetSource.setBeanFactory(this.beanFactory);
              proxyFactory.setTargetSource(targetSource);

              Object proxy = proxyFactory.getProxy(this.beanClassLoader);
              ObjectName objectName = getObjectName(proxy, beanKey);
              if (logger.isDebugEnabled()) {
                  logger.debug("Located simple bean '" + beanKey + "': registering with JMX server as lazy-init MBean [" +
                          objectName + "]");
              }
              ModelMBean mbean = createAndConfigureMBean(proxy, beanKey);
              targetSource.setModelMBean(mbean);
              targetSource.setObjectName(objectName);
              doRegister(mbean, objectName);
              return objectName;
          }*/
        return null;
    }

    /**
     * Retrieve the <code>ObjectName</code> for a bean.
     * <p>If the bean implements the <code>SelfNaming</code> interface, then the
     * <code>ObjectName</code> will be retrieved using <code>SelfNaming.getObjectName()</code>.
     * Otherwise, the configured <code>ObjectNamingStrategy</code> is used.
     *
     * @param bean    the name of the bean in the <code>BeanFactory</code>
     * @param beanKey the key associated with the bean in the beans map
     * @return the <code>ObjectName</code> for the supplied bean
     * @throws javax.management.MalformedObjectNameException
     *          if the retrieved <code>ObjectName</code> is malformed
     */
    protected ObjectName getObjectName(Object bean, String beanKey) throws MalformedObjectNameException {
        if (bean instanceof SelfNaming) {
            return ((SelfNaming) bean).getObjectName();
        } else {
            return this.namingStrategy.getObjectName(bean, beanKey);
        }
    }

    /**
     * Determine whether the given bean class qualifies as an MBean as-is.
     * <p>The default implementation delegates to {@link tmp.spring.support.JmxUtils#isMBean},
     * which checks for {@link javax.management.DynamicMBean} classes as well
     * as classes with corresponding "*MBean" interface (Standard MBeans)
     * or corresponding "*MXBean" interface (Java 6 MXBeans).
     *
     * @param beanClass the bean class to analyze
     * @return whether the class qualifies as an MBean
     * @see tmp.spring.support.JmxUtils#isMBean(Class)
     */
    protected boolean isMBean(Class beanClass) {
        return JmxUtils.isMBean(beanClass);
    }

    /**
     * Build an adapted MBean for the given bean instance, if possible.
     * <p>The default implementation builds a JMX 1.2 StandardMBean
     * for the target's MBean/MXBean interface in case of an AOP proxy,
     * delegating the interface's management operations to the proxy.
     *
     * @param bean the original bean instance
     * @return the adapted MBean, or <code>null</code> if not possible
     */
    @SuppressWarnings("unchecked")
    protected DynamicMBean adaptMBeanIfPossible(Object bean) throws JMException {
        Class targetClass = AopUtils.getTargetClass(bean);
        if (targetClass != bean.getClass()) {
            Class ifc = JmxUtils.getMXBeanInterface(targetClass);
            if (ifc != null) {
                if (!(ifc.isInstance(bean))) {
                    throw new NotCompliantMBeanException("Managed bean [" + bean +
                            "] has a target class with an MXBean interface but does not expose it in the proxy");
                }
                return new StandardMBean(bean, ifc, true);
            } else {
                ifc = JmxUtils.getMBeanInterface(targetClass);
                if (ifc != null) {
                    if (!(ifc.isInstance(bean))) {
                        throw new NotCompliantMBeanException("Managed bean [" + bean +
                                "] has a target class with an MBean interface but does not expose it in the proxy");
                    }
                    return new StandardMBean(bean, ifc);
                }
            }
        }
        return null;
    }

    /**
     * Creates an MBean that is configured with the appropriate management
     * interface for the supplied managed resource.
     *
     * @param managedResource the resource that is to be exported as an MBean
     * @param beanKey         the key associated with the managed bean
     * @see #createModelMBean()
     * @see #getMBeanInfo(Object, String)
     */
    protected ModelMBean createAndConfigureMBean(Object managedResource, String beanKey)
            throws MBeanExportException {
        try {
            ModelMBean mbean = createModelMBean();
            mbean.setModelMBeanInfo(getMBeanInfo(managedResource, beanKey));
            mbean.setManagedResource(managedResource, MR_TYPE_OBJECT_REFERENCE);
            return mbean;
        }
        catch (Exception ex) {
            throw new MBeanExportException("Could not create ModelMBean for managed resource [" +
                    managedResource + "] with key '" + beanKey + "'", ex);
        }
    }

    /**
     * Create an instance of a class that implements <code>ModelMBean</code>.
     * <p>This method is called to obtain a <code>ModelMBean</code> instance to
     * use when registering a bean. This method is called once per bean during the
     * registration phase and must return a new instance of <code>ModelMBean</code>
     *
     * @return a new instance of a class that implements <code>ModelMBean</code>
     * @throws javax.management.MBeanException
     *          if creation of the ModelMBean failed
     */
    protected ModelMBean createModelMBean() throws MBeanException {
        return (this.exposeManagedResourceClassLoader ? new SpringModelMBean() : new RequiredModelMBean());
    }

    /**
     * Gets the <code>ModelMBeanInfo</code> for the bean with the supplied key
     * and of the supplied type.
     */
    private ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey) throws JMException {
        ModelMBeanInfo info = this.assembler.getMBeanInfo(managedBean, beanKey);
        /*if (logger.isWarnEnabled() && ObjectUtils.isEmpty(info.getAttributes()) &&
                  ObjectUtils.isEmpty(info.getOperations())) {
              logger.warn("Bean with key '" + beanKey +
                      "' has been registered as an MBean but has no exposed attributes or operations");
          }*/
        return info;
    }


    //---------------------------------------------------------------------
    // Autodetection process
    //---------------------------------------------------------------------

    /**
     * Invoked when using an <code>AutodetectCapableMBeanInfoAssembler</code>.
     * Gives the assembler the opportunity to add additional beans from the
     * <code>BeanFactory</code> to the list of beans to be exposed via JMX.
     * <p>This implementation prevents a bean from being added to the list
     * automatically if it has already been added manually, and it prevents
     * certain internal classes from being registered automatically.
     */
    private void autodetectBeans(final AutodetectCapableMBeanInfoAssembler assembler) {
        autodetect(new AutodetectCallback() {
            public boolean include(Class beanClass, String beanName) {
                return assembler.includeBean(beanClass, beanName);
            }
        });
    }

    /**
     * Attempts to detect any beans defined in the <code>ApplicationContext</code> that are
     * valid MBeans and registers them automatically with the <code>MBeanServer</code>.
     */
    private void autodetectMBeans() {
        autodetect(new AutodetectCallback() {
            public boolean include(Class beanClass, String beanName) {
                return isMBean(beanClass);
            }
        });
    }

    /**
     * Performs the actual autodetection process, delegating to an
     * <code>AutodetectCallback</code> instance to vote on the inclusion of a
     * given bean.
     *
     * @param callback the <code>AutodetectCallback</code> to use when deciding
     *                 whether to include a bean or not
     */
    private void autodetect(AutodetectCallback callback) {
        /*Set<String> beanNames = new LinkedHashSet<String>(this.beanFactory.getBeanDefinitionCount());
          beanNames.addAll(Arrays.asList(this.beanFactory.getBeanDefinitionNames()));
          if (this.beanFactory instanceof ConfigurableBeanFactory) {
              beanNames.addAll(Arrays.asList(((ConfigurableBeanFactory) this.beanFactory).getSingletonNames()));
          }
          for (String beanName : beanNames) {
              if (!isExcluded(beanName)) {
                  try {
                      Class beanClass = this.beanFactory.getType(beanName);
                      if (beanClass != null && callback.include(beanClass, beanName)) {
                          boolean lazyInit = isBeanDefinitionLazyInit(this.beanFactory, beanName);
                          Object beanInstance = (!lazyInit ? this.beanFactory.getBean(beanName) : null);
                          if (!this.beans.containsValue(beanName) && (beanInstance == null ||
                                  !CollectionUtils.containsInstance(this.beans.values(), beanInstance))) {
                              // Not already registered for JMX exposure.
                              this.beans.put(beanName, (beanInstance != null ? beanInstance : beanName));
                              if (logger.isInfoEnabled()) {
                                  logger.info("Bean with name '" + beanName + "' has been autodetected for JMX exposure");
                              }
                          }
                          else {
                              if (logger.isDebugEnabled()) {
                                  logger.debug("Bean with name '" + beanName + "' is already registered for JMX exposure");
                              }
                          }
                      }
                  }
                  catch (CannotLoadBeanClassException ex) {
                      if (this.allowEagerInit) {
                          throw ex;
                      }
                      // otherwise ignore beans where the class is not resolvable
                  }
              }
          }*/
    }

    /**
     * Indicates whether or not a particular bean name is present in the excluded beans list.
     */
    private boolean isExcluded(String beanName) {
        return (this.excludedBeans != null &&
                (this.excludedBeans.contains(beanName) /*||
						(beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX) &&
								this.excludedBeans.contains(beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length())))*/));
    }

    //---------------------------------------------------------------------
    // Inner classes for internal use
    //---------------------------------------------------------------------

    /**
     * Internal callback interface for the autodetection process.
     */
    private static interface AutodetectCallback {

        /**
         * Called during the autodetection process to decide whether
         * or not a bean should be included.
         *
         * @param beanClass the class of the bean
         * @param beanName  the name of the bean
         */
        boolean include(Class beanClass, String beanName);
    }


}