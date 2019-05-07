private Scheduler instantiate() throws SchedulerException {
    if (cfg == null) {
        initialize();
    }
    if (initException != null) {
        throw initException;
    }
    JobStore js = null;
    ThreadPool tp = null;
    QuartzScheduler qs = null;
    SchedulingContext schedCtxt = null;
    DBConnectionManager dbMgr = null;
    String instanceIdGeneratorClass = null;
    Properties tProps = null;
    String userTXLocation = null;
    boolean wrapJobInTx = false;
    boolean autoId = false;
    long idleWaitTime = -1;
    long dbFailureRetry = -1;
    String classLoadHelperClass;
    String jobFactoryClass;
    SchedulerRepository schedRep = SchedulerRepository.getInstance();
    // Get Scheduler Properties 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
    String schedName = cfg.getStringProperty(PROP_SCHED_INSTANCE_NAME, "QuartzScheduler");
    String threadName = cfg.getStringProperty(PROP_SCHED_THREAD_NAME, schedName + "_QuartzSchedulerThread");
    String schedInstId = cfg.getStringProperty(PROP_SCHED_INSTANCE_ID, DEFAULT_INSTANCE_ID);
    if (schedInstId.equals(AUTO_GENERATE_INSTANCE_ID)) {
        autoId = true;
        instanceIdGeneratorClass = cfg.getStringProperty(PROP_SCHED_INSTANCE_ID_GENERATOR_CLASS, "org.quartz.simpl.SimpleInstanceIdGenerator");
    }
    userTXLocation = cfg.getStringProperty(PROP_SCHED_USER_TX_URL, userTXLocation);
    if (userTXLocation != null && userTXLocation.trim().length() == 0) {
        userTXLocation = null;
    }
    classLoadHelperClass = cfg.getStringProperty(PROP_SCHED_CLASS_LOAD_HELPER_CLASS, "org.quartz.simpl.CascadingClassLoadHelper");
    wrapJobInTx = cfg.getBooleanProperty(PROP_SCHED_WRAP_JOB_IN_USER_TX, wrapJobInTx);
    jobFactoryClass = cfg.getStringProperty(PROP_SCHED_JOB_FACTORY_CLASS, null);
    idleWaitTime = cfg.getLongProperty(PROP_SCHED_IDLE_WAIT_TIME, idleWaitTime);
    dbFailureRetry = cfg.getLongProperty(PROP_SCHED_DB_FAILURE_RETRY_INTERVAL, dbFailureRetry);
    boolean makeSchedulerThreadDaemon = cfg.getBooleanProperty(PROP_SCHED_MAKE_SCHEDULER_THREAD_DAEMON);
    boolean threadsInheritInitalizersClassLoader = cfg.getBooleanProperty(PROP_SCHED_SCHEDULER_THREADS_INHERIT_CONTEXT_CLASS_LOADER_OF_INITIALIZING_THREAD);
    boolean skipUpdateCheck = cfg.getBooleanProperty(PROP_SCHED_SKIP_UPDATE_CHECK, false);
    boolean interruptJobsOnShutdown = cfg.getBooleanProperty(PROP_SCHED_INTERRUPT_JOBS_ON_SHUTDOWN, false);
    boolean interruptJobsOnShutdownWithWait = cfg.getBooleanProperty(PROP_SCHED_INTERRUPT_JOBS_ON_SHUTDOWN_WITH_WAIT, false);
    boolean jmxExport = cfg.getBooleanProperty(PROP_SCHED_JMX_EXPORT);
    String jmxObjectName = cfg.getStringProperty(PROP_SCHED_JMX_OBJECT_NAME);
    boolean jmxProxy = cfg.getBooleanProperty(PROP_SCHED_JMX_PROXY);
    String jmxProxyClass = cfg.getStringProperty(PROP_SCHED_JMX_PROXY_CLASS);
    boolean rmiExport = cfg.getBooleanProperty(PROP_SCHED_RMI_EXPORT, false);
    boolean rmiProxy = cfg.getBooleanProperty(PROP_SCHED_RMI_PROXY, false);
    String rmiHost = cfg.getStringProperty(PROP_SCHED_RMI_HOST, "localhost");
    int rmiPort = cfg.getIntProperty(PROP_SCHED_RMI_PORT, 1099);
    int rmiServerPort = cfg.getIntProperty(PROP_SCHED_RMI_SERVER_PORT, -1);
    String rmiCreateRegistry = cfg.getStringProperty(PROP_SCHED_RMI_CREATE_REGISTRY, QuartzSchedulerResources.CREATE_REGISTRY_NEVER);
    String rmiBindName = cfg.getStringProperty(PROP_SCHED_RMI_BIND_NAME);
    if (jmxProxy && rmiProxy) {
        throw new SchedulerConfigException("Cannot proxy both RMI and JMX.");
    }
    Properties schedCtxtProps = cfg.getPropertyGroup(PROP_SCHED_CONTEXT_PREFIX, true);
    // If Proxying to remote scheduler, short-circuit here... 
    // ~~~~~~~~~~~~~~~~~~ 
    if (rmiProxy) {
        if (autoId) {
            schedInstId = DEFAULT_INSTANCE_ID;
        }
        schedCtxt = new SchedulingContext();
        schedCtxt.setInstanceId(schedInstId);
        String uid = (rmiBindName == null) ? QuartzSchedulerResources.getUniqueIdentifier(schedName, schedInstId) : rmiBindName;
        RemoteScheduler remoteScheduler = new RemoteScheduler(schedCtxt, uid, rmiHost, rmiPort);
        schedRep.bind(remoteScheduler);
        return remoteScheduler;
    }
    // Create class load helper 
    ClassLoadHelper loadHelper = null;
    try {
        loadHelper = (ClassLoadHelper) loadClass(classLoadHelperClass).newInstance();
    } catch (Exception e) {
        throw new SchedulerConfigException("Unable to instantiate class load helper class: " + e.getMessage(), e);
    }
    loadHelper.initialize();
    // If Proxying to remote JMX scheduler, short-circuit here... 
    // ~~~~~~~~~~~~~~~~~~ 
    if (jmxProxy) {
        if (autoId) {
            schedInstId = DEFAULT_INSTANCE_ID;
        }
        if (jmxProxyClass == null) {
            throw new SchedulerConfigException("No JMX Proxy Scheduler class provided");
        }
        RemoteMBeanScheduler jmxScheduler = null;
        try {
            jmxScheduler = (RemoteMBeanScheduler) loadHelper.loadClass(jmxProxyClass).newInstance();
        } catch (Exception e) {
            throw new SchedulerConfigException("Unable to instantiate RemoteMBeanScheduler class.", e);
        }
        schedCtxt = new SchedulingContext();
        schedCtxt.setInstanceId(schedInstId);
        if (jmxObjectName == null) {
            jmxObjectName = QuartzSchedulerResources.generateJMXObjectName(schedName, schedInstId);
        }
        jmxScheduler.setSchedulingContext(schedCtxt);
        jmxScheduler.setSchedulerObjectName(jmxObjectName);
        tProps = cfg.getPropertyGroup(PROP_SCHED_JMX_PROXY, true);
        try {
            setBeanProps(jmxScheduler, tProps);
        } catch (Exception e) {
            initException = new SchedulerException("RemoteMBeanScheduler class '" + jmxProxyClass + "' props could not be configured.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        jmxScheduler.initialize();
        schedRep.bind(jmxScheduler);
        return jmxScheduler;
    }
    JobFactory jobFactory = null;
    if (jobFactoryClass != null) {
        try {
            jobFactory = (JobFactory) loadHelper.loadClass(jobFactoryClass).newInstance();
        } catch (Exception e) {
            throw new SchedulerConfigException("Unable to instantiate JobFactory class: " + e.getMessage(), e);
        }
        tProps = cfg.getPropertyGroup(PROP_SCHED_JOB_FACTORY_PREFIX, true);
        try {
            setBeanProps(jobFactory, tProps);
        } catch (Exception e) {
            initException = new SchedulerException("JobFactory class '" + jobFactoryClass + "' props could not be configured.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
    }
    InstanceIdGenerator instanceIdGenerator = null;
    if (instanceIdGeneratorClass != null) {
        try {
            instanceIdGenerator = (InstanceIdGenerator) loadHelper.loadClass(instanceIdGeneratorClass).newInstance();
        } catch (Exception e) {
            throw new SchedulerConfigException("Unable to instantiate InstanceIdGenerator class: " + e.getMessage(), e);
        }
        tProps = cfg.getPropertyGroup(PROP_SCHED_INSTANCE_ID_GENERATOR_PREFIX, true);
        try {
            setBeanProps(instanceIdGenerator, tProps);
        } catch (Exception e) {
            initException = new SchedulerException("InstanceIdGenerator class '" + instanceIdGeneratorClass + "' props could not be configured.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
    }
    // Get ThreadPool Properties 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
    String tpClass = cfg.getStringProperty(PROP_THREAD_POOL_CLASS, null);
    if (tpClass == null) {
        initException = new SchedulerException("ThreadPool class not specified. ", SchedulerException.ERR_BAD_CONFIGURATION);
        throw initException;
    }
    try {
        tp = (ThreadPool) loadHelper.loadClass(tpClass).newInstance();
    } catch (Exception e) {
        initException = new SchedulerException("ThreadPool class '" + tpClass + "' could not be instantiated.", e);
        initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
        throw initException;
    }
    tProps = cfg.getPropertyGroup(PROP_THREAD_POOL_PREFIX, true);
    try {
        setBeanProps(tp, tProps);
    } catch (Exception e) {
        initException = new SchedulerException("ThreadPool class '" + tpClass + "' props could not be configured.", e);
        initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
        throw initException;
    }
    // Get JobStore Properties 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
    String jsClass = cfg.getStringProperty(PROP_JOB_STORE_CLASS, RAMJobStore.class.getName());
    if (jsClass == null) {
        initException = new SchedulerException("JobStore class not specified. ", SchedulerException.ERR_BAD_CONFIGURATION);
        throw initException;
    }
    try {
        js = (JobStore) loadHelper.loadClass(jsClass).newInstance();
    } catch (Exception e) {
        initException = new SchedulerException("JobStore class '" + jsClass + "' could not be instantiated.", e);
        initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
        throw initException;
    }
    SchedulerDetailsSetter.setDetails(js, schedName, schedInstId);
    tProps = cfg.getPropertyGroup(PROP_JOB_STORE_PREFIX, true, new String[] { PROP_JOB_STORE_LOCK_HANDLER_PREFIX });
    try {
        setBeanProps(js, tProps);
    } catch (Exception e) {
        initException = new SchedulerException("JobStore class '" + jsClass + "' props could not be configured.", e);
        initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
        throw initException;
    }
    if (js instanceof JobStoreSupport) {
        // Install custom lock handler (Semaphore) 
        String lockHandlerClass = cfg.getStringProperty(PROP_JOB_STORE_LOCK_HANDLER_CLASS);
        if (lockHandlerClass != null) {
            try {
                Semaphore lockHandler = (Semaphore) loadHelper.loadClass(lockHandlerClass).newInstance();
                tProps = cfg.getPropertyGroup(PROP_JOB_STORE_LOCK_HANDLER_PREFIX, true);
                // If this lock handler requires the table prefix, add it to its properties. 
                if (lockHandler instanceof TablePrefixAware) {
                    tProps.setProperty(PROP_TABLE_PREFIX, ((JobStoreSupport) js).getTablePrefix());
                }
                try {
                    setBeanProps(lockHandler, tProps);
                } catch (Exception e) {
                    initException = new SchedulerException("JobStore LockHandler class '" + lockHandlerClass + "' props could not be configured.", e);
                    initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                    throw initException;
                }
                ((JobStoreSupport) js).setLockHandler(lockHandler);
                getLog().info("Using custom data access locking (synchronization): " + lockHandlerClass);
            } catch (Exception e) {
                initException = new SchedulerException("JobStore LockHandler class '" + lockHandlerClass + "' could not be instantiated.", e);
                initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
        }
    }
    // Set up any DataSources 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
    String[] dsNames = cfg.getPropertyGroups(PROP_DATASOURCE_PREFIX);
    for (int i = 0; i < dsNames.length; i++) {
        PropertiesParser pp = new PropertiesParser(cfg.getPropertyGroup(PROP_DATASOURCE_PREFIX + "." + dsNames[i], true));
        String cpClass = pp.getStringProperty(PROP_CONNECTION_PROVIDER_CLASS, null);
        // custom connectionProvider... 
        if (cpClass != null) {
            ConnectionProvider cp = null;
            try {
                cp = (ConnectionProvider) loadHelper.loadClass(cpClass).newInstance();
            } catch (Exception e) {
                initException = new SchedulerException("ConnectionProvider class '" + cpClass + "' could not be instantiated.", e);
                initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            try {
                // remove the class name, so it isn't attempted to be set 
                pp.getUnderlyingProperties().remove(PROP_CONNECTION_PROVIDER_CLASS);
                setBeanProps(cp, pp.getUnderlyingProperties());
            } catch (Exception e) {
                initException = new SchedulerException("ConnectionProvider class '" + cpClass + "' props could not be configured.", e);
                initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            dbMgr = DBConnectionManager.getInstance();
            dbMgr.addConnectionProvider(dsNames[i], cp);
        } else {
            String dsJndi = pp.getStringProperty(PROP_DATASOURCE_JNDI_URL, null);
            if (dsJndi != null) {
                boolean dsAlwaysLookup = pp.getBooleanProperty(PROP_DATASOURCE_JNDI_ALWAYS_LOOKUP);
                String dsJndiInitial = pp.getStringProperty(PROP_DATASOURCE_JNDI_INITIAL);
                String dsJndiProvider = pp.getStringProperty(PROP_DATASOURCE_JNDI_PROVDER);
                String dsJndiPrincipal = pp.getStringProperty(PROP_DATASOURCE_JNDI_PRINCIPAL);
                String dsJndiCredentials = pp.getStringProperty(PROP_DATASOURCE_JNDI_CREDENTIALS);
                Properties props = null;
                if (null != dsJndiInitial || null != dsJndiProvider || null != dsJndiPrincipal || null != dsJndiCredentials) {
                    props = new Properties();
                    if (dsJndiInitial != null) {
                        props.put(PROP_DATASOURCE_JNDI_INITIAL, dsJndiInitial);
                    }
                    if (dsJndiProvider != null) {
                        props.put(PROP_DATASOURCE_JNDI_PROVDER, dsJndiProvider);
                    }
                    if (dsJndiPrincipal != null) {
                        props.put(PROP_DATASOURCE_JNDI_PRINCIPAL, dsJndiPrincipal);
                    }
                    if (dsJndiCredentials != null) {
                        props.put(PROP_DATASOURCE_JNDI_CREDENTIALS, dsJndiCredentials);
                    }
                }
                JNDIConnectionProvider cp = new JNDIConnectionProvider(dsJndi, props, dsAlwaysLookup);
                dbMgr = DBConnectionManager.getInstance();
                dbMgr.addConnectionProvider(dsNames[i], cp);
            } else {
                String dsDriver = pp.getStringProperty(PROP_DATASOURCE_DRIVER);
                String dsURL = pp.getStringProperty(PROP_DATASOURCE_URL);
                String dsUser = pp.getStringProperty(PROP_DATASOURCE_USER, "");
                String dsPass = pp.getStringProperty(PROP_DATASOURCE_PASSWORD, "");
                int dsCnt = pp.getIntProperty(PROP_DATASOURCE_MAX_CONNECTIONS, 10);
                String dsValidation = pp.getStringProperty(PROP_DATASOURCE_VALIDATION_QUERY);
                if (dsDriver == null) {
                    initException = new SchedulerException("Driver not specified for DataSource: " + dsNames[i]);
                    throw initException;
                }
                if (dsURL == null) {
                    initException = new SchedulerException("DB URL not specified for DataSource: " + dsNames[i]);
                    throw initException;
                }
                try {
                    PoolingConnectionProvider cp = new PoolingConnectionProvider(dsDriver, dsURL, dsUser, dsPass, dsCnt, dsValidation);
                    dbMgr = DBConnectionManager.getInstance();
                    dbMgr.addConnectionProvider(dsNames[i], cp);
                } catch (SQLException sqle) {
                    initException = new SchedulerException("Could not initialize DataSource: " + dsNames[i], sqle);
                    throw initException;
                }
            }
        }
    }
    // Set up any SchedulerPlugins 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
    String[] pluginNames = cfg.getPropertyGroups(PROP_PLUGIN_PREFIX);
    SchedulerPlugin[] plugins = new SchedulerPlugin[pluginNames.length];
    for (int i = 0; i < pluginNames.length; i++) {
        Properties pp = cfg.getPropertyGroup(PROP_PLUGIN_PREFIX + "." + pluginNames[i], true);
        String plugInClass = pp.getProperty(PROP_PLUGIN_CLASS, null);
        if (plugInClass == null) {
            initException = new SchedulerException("SchedulerPlugin class not specified for plugin '" + pluginNames[i] + "'", SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        SchedulerPlugin plugin = null;
        try {
            plugin = (SchedulerPlugin) loadHelper.loadClass(plugInClass).newInstance();
        } catch (Exception e) {
            initException = new SchedulerException("SchedulerPlugin class '" + plugInClass + "' could not be instantiated.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        try {
            setBeanProps(plugin, pp);
        } catch (Exception e) {
            initException = new SchedulerException("JobStore SchedulerPlugin '" + plugInClass + "' props could not be configured.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        plugins[i] = plugin;
    }
    // Set up any JobListeners 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
    Class[] strArg = new Class[] { String.class };
    String[] jobListenerNames = cfg.getPropertyGroups(PROP_JOB_LISTENER_PREFIX);
    JobListener[] jobListeners = new JobListener[jobListenerNames.length];
    for (int i = 0; i < jobListenerNames.length; i++) {
        Properties lp = cfg.getPropertyGroup(PROP_JOB_LISTENER_PREFIX + "." + jobListenerNames[i], true);
        String listenerClass = lp.getProperty(PROP_LISTENER_CLASS, null);
        if (listenerClass == null) {
            initException = new SchedulerException("JobListener class not specified for listener '" + jobListenerNames[i] + "'", SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        JobListener listener = null;
        try {
            listener = (JobListener) loadHelper.loadClass(listenerClass).newInstance();
        } catch (Exception e) {
            initException = new SchedulerException("JobListener class '" + listenerClass + "' could not be instantiated.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        try {
            Method nameSetter = listener.getClass().getMethod("setName", strArg);
            if (nameSetter != null) {
                nameSetter.invoke(listener, new Object[] { jobListenerNames[i] });
            }
            setBeanProps(listener, lp);
        } catch (Exception e) {
            initException = new SchedulerException("JobListener '" + listenerClass + "' props could not be configured.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        jobListeners[i] = listener;
    }
    // Set up any TriggerListeners 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
    String[] triggerListenerNames = cfg.getPropertyGroups(PROP_TRIGGER_LISTENER_PREFIX);
    TriggerListener[] triggerListeners = new TriggerListener[triggerListenerNames.length];
    for (int i = 0; i < triggerListenerNames.length; i++) {
        Properties lp = cfg.getPropertyGroup(PROP_TRIGGER_LISTENER_PREFIX + "." + triggerListenerNames[i], true);
        String listenerClass = lp.getProperty(PROP_LISTENER_CLASS, null);
        if (listenerClass == null) {
            initException = new SchedulerException("TriggerListener class not specified for listener '" + triggerListenerNames[i] + "'", SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        TriggerListener listener = null;
        try {
            listener = (TriggerListener) loadHelper.loadClass(listenerClass).newInstance();
        } catch (Exception e) {
            initException = new SchedulerException("TriggerListener class '" + listenerClass + "' could not be instantiated.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        try {
            Method nameSetter = listener.getClass().getMethod("setName", strArg);
            if (nameSetter != null) {
                nameSetter.invoke(listener, new Object[] { triggerListenerNames[i] });
            }
            setBeanProps(listener, lp);
        } catch (Exception e) {
            initException = new SchedulerException("TriggerListener '" + listenerClass + "' props could not be configured.", e);
            initException.setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        triggerListeners[i] = listener;
    }
    boolean tpInited = false;
    boolean qsInited = false;
    // Fire everything up 
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
    try {
        JobRunShellFactory jrsf = null;
        // Create correct run-shell factory... 
        if (userTXLocation != null) {
            UserTransactionHelper.setUserTxLocation(userTXLocation);
        }
        if (wrapJobInTx) {
            jrsf = new JTAJobRunShellFactory();
        } else {
            jrsf = new StdJobRunShellFactory();
        }
        if (autoId) {
            try {
                schedInstId = DEFAULT_INSTANCE_ID;
                if (js.isClustered()) {
                    if (js.getClass().getName().equals("org.terracotta.quartz.TerracottaJobStore")) {
                        Class c = js.getClass();
                        Method m = c.getMethod("getUUID");
                        if (m == null) {
                            throw new RuntimeException("TerracottaJobStore does not have expected UUID property.");
                        }
                        String uuid = (String) m.invoke(js);
                        schedInstId = "TERRACOTTA_CLUSTERED_SCHEDULER,node=" + uuid;
                    } else {
                        schedInstId = instanceIdGenerator.generateInstanceId();
                    }
                }
            } catch (Exception e) {
                getLog().error("Couldn't generate instance Id!", e);
                throw new IllegalStateException("Cannot run without an instance id.");
            }
        }
        if (js instanceof JobStoreSupport) {
            JobStoreSupport jjs = (JobStoreSupport) js;
            jjs.setDbRetryInterval(dbFailureRetry);
            if (threadsInheritInitalizersClassLoader)
                jjs.setThreadsInheritInitializersClassLoadContext(threadsInheritInitalizersClassLoader);
        }
        QuartzSchedulerResources rsrcs = new QuartzSchedulerResources();
        rsrcs.setName(schedName);
        rsrcs.setThreadName(threadName);
        rsrcs.setInstanceId(schedInstId);
        rsrcs.setJobRunShellFactory(jrsf);
        rsrcs.setMakeSchedulerThreadDaemon(makeSchedulerThreadDaemon);
        rsrcs.setThreadsInheritInitializersClassLoadContext(threadsInheritInitalizersClassLoader);
        rsrcs.setRunUpdateCheck(!skipUpdateCheck);
        rsrcs.setInterruptJobsOnShutdown(interruptJobsOnShutdownWithWait);
        rsrcs.setInterruptJobsOnShutdownWithWait(interruptJobsOnShutdownWithWait);
        rsrcs.setJMXExport(jmxExport);
        rsrcs.setJMXObjectName(jmxObjectName);
        if (rmiExport) {
            rsrcs.setRMIRegistryHost(rmiHost);
            rsrcs.setRMIRegistryPort(rmiPort);
            rsrcs.setRMIServerPort(rmiServerPort);
            rsrcs.setRMICreateRegistryStrategy(rmiCreateRegistry);
            rsrcs.setRMIBindName(rmiBindName);
        }
        SchedulerDetailsSetter.setDetails(tp, schedName, schedInstId);
        rsrcs.setThreadPool(tp);
        if (tp instanceof SimpleThreadPool) {
            ((SimpleThreadPool) tp).setThreadNamePrefix(schedName + "_Worker");
            if (threadsInheritInitalizersClassLoader)
                ((SimpleThreadPool) tp).setThreadsInheritContextClassLoaderOfInitializingThread(threadsInheritInitalizersClassLoader);
        }
        tp.initialize();
        tpInited = true;
        rsrcs.setJobStore(js);
        // add plugins 
        for (int i = 0; i < plugins.length; i++) {
            rsrcs.addSchedulerPlugin(plugins[i]);
        }
        schedCtxt = new SchedulingContext();
        schedCtxt.setInstanceId(rsrcs.getInstanceId());
        qs = new QuartzScheduler(rsrcs, schedCtxt, idleWaitTime, dbFailureRetry);
        qsInited = true;
        // Create Scheduler ref... 
        Scheduler scheduler = instantiate(rsrcs, qs);
        // set job factory if specified 
        if (jobFactory != null) {
            qs.setJobFactory(jobFactory);
        }
        // Initialize plugins now that we have a Scheduler instance. 
        for (int i = 0; i < plugins.length; i++) {
            plugins[i].initialize(pluginNames[i], scheduler);
        }
        // add listeners 
        for (int i = 0; i < jobListeners.length; i++) {
            qs.addGlobalJobListener(jobListeners[i]);
        }
        for (int i = 0; i < triggerListeners.length; i++) {
            qs.addGlobalTriggerListener(triggerListeners[i]);
        }
        // set scheduler context data... 
        Iterator itr = schedCtxtProps.keySet().iterator();
        while (itr.hasNext()) {
            String key = (String) itr.next();
            String val = schedCtxtProps.getProperty(key);
            scheduler.getContext().put(key, val);
        }
        // fire up job store, and runshell factory 
        js.setInstanceId(schedInstId);
        js.setInstanceName(schedName);
        js.initialize(loadHelper, qs.getSchedulerSignaler());
        jrsf.initialize(scheduler, schedCtxt);
        qs.initialize();
        getLog().info("Quartz scheduler '" + scheduler.getSchedulerName() + "' initialized from " + propSrc);
        getLog().info("Quartz scheduler version: " + qs.getVersion());
        // prevents the repository from being garbage collected 
        qs.addNoGCObject(schedRep);
        // prevents the db manager from being garbage collected 
        if (dbMgr != null) {
            qs.addNoGCObject(dbMgr);
        }
        schedRep.bind(scheduler);
        return scheduler;
    } catch (SchedulerException e) {
        if (qsInited)
            qs.shutdown(false);
        else if (tpInited)
            tp.shutdown(false);
        throw e;
    } catch (RuntimeException re) {
        if (qsInited)
            qs.shutdown(false);
        else if (tpInited)
            tp.shutdown(false);
        throw re;
    } catch (Error re) {
        if (qsInited)
            qs.shutdown(false);
        else if (tpInited)
            tp.shutdown(false);
        throw re;
    }
}
