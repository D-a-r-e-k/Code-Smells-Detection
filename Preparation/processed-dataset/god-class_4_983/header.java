void method0() { 
/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constants.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
public static final String PROPERTIES_FILE = "org.quartz.properties";
public static final String PROP_SCHED_INSTANCE_NAME = "org.quartz.scheduler.instanceName";
public static final String PROP_SCHED_INSTANCE_ID = "org.quartz.scheduler.instanceId";
public static final String PROP_SCHED_INSTANCE_ID_GENERATOR_PREFIX = "org.quartz.scheduler.instanceIdGenerator";
public static final String PROP_SCHED_INSTANCE_ID_GENERATOR_CLASS = PROP_SCHED_INSTANCE_ID_GENERATOR_PREFIX + ".class";
public static final String PROP_SCHED_THREAD_NAME = "org.quartz.scheduler.threadName";
public static final String PROP_SCHED_SKIP_UPDATE_CHECK = "org.quartz.scheduler.skipUpdateCheck";
public static final String PROP_SCHED_JMX_EXPORT = "org.quartz.scheduler.jmx.export";
public static final String PROP_SCHED_JMX_OBJECT_NAME = "org.quartz.scheduler.jmx.objectName";
public static final String PROP_SCHED_JMX_PROXY = "org.quartz.scheduler.jmx.proxy";
public static final String PROP_SCHED_JMX_PROXY_CLASS = "org.quartz.scheduler.jmx.proxy.class";
public static final String PROP_SCHED_RMI_EXPORT = "org.quartz.scheduler.rmi.export";
public static final String PROP_SCHED_RMI_PROXY = "org.quartz.scheduler.rmi.proxy";
public static final String PROP_SCHED_RMI_HOST = "org.quartz.scheduler.rmi.registryHost";
public static final String PROP_SCHED_RMI_PORT = "org.quartz.scheduler.rmi.registryPort";
public static final String PROP_SCHED_RMI_SERVER_PORT = "org.quartz.scheduler.rmi.serverPort";
public static final String PROP_SCHED_RMI_CREATE_REGISTRY = "org.quartz.scheduler.rmi.createRegistry";
public static final String PROP_SCHED_RMI_BIND_NAME = "org.quartz.scheduler.rmi.bindName";
public static final String PROP_SCHED_WRAP_JOB_IN_USER_TX = "org.quartz.scheduler.wrapJobExecutionInUserTransaction";
public static final String PROP_SCHED_USER_TX_URL = "org.quartz.scheduler.userTransactionURL";
public static final String PROP_SCHED_IDLE_WAIT_TIME = "org.quartz.scheduler.idleWaitTime";
public static final String PROP_SCHED_DB_FAILURE_RETRY_INTERVAL = "org.quartz.scheduler.dbFailureRetryInterval";
public static final String PROP_SCHED_MAKE_SCHEDULER_THREAD_DAEMON = "org.quartz.scheduler.makeSchedulerThreadDaemon";
public static final String PROP_SCHED_SCHEDULER_THREADS_INHERIT_CONTEXT_CLASS_LOADER_OF_INITIALIZING_THREAD = "org.quartz.scheduler.threadsInheritContextClassLoaderOfInitializer";
public static final String PROP_SCHED_CLASS_LOAD_HELPER_CLASS = "org.quartz.scheduler.classLoadHelper.class";
public static final String PROP_SCHED_JOB_FACTORY_CLASS = "org.quartz.scheduler.jobFactory.class";
public static final String PROP_SCHED_JOB_FACTORY_PREFIX = "org.quartz.scheduler.jobFactory";
public static final String PROP_SCHED_INTERRUPT_JOBS_ON_SHUTDOWN = "org.quartz.scheduler.interruptJobsOnShutdown";
public static final String PROP_SCHED_INTERRUPT_JOBS_ON_SHUTDOWN_WITH_WAIT = "org.quartz.scheduler.interruptJobsOnShutdownWithWait";
public static final String PROP_SCHED_CONTEXT_PREFIX = "org.quartz.context.key";
public static final String PROP_THREAD_POOL_PREFIX = "org.quartz.threadPool";
public static final String PROP_THREAD_POOL_CLASS = "org.quartz.threadPool.class";
public static final String PROP_JOB_STORE_PREFIX = "org.quartz.jobStore";
public static final String PROP_JOB_STORE_LOCK_HANDLER_PREFIX = PROP_JOB_STORE_PREFIX + ".lockHandler";
public static final String PROP_JOB_STORE_LOCK_HANDLER_CLASS = PROP_JOB_STORE_LOCK_HANDLER_PREFIX + ".class";
public static final String PROP_TABLE_PREFIX = "tablePrefix";
public static final String PROP_JOB_STORE_CLASS = "org.quartz.jobStore.class";
public static final String PROP_JOB_STORE_USE_PROP = "org.quartz.jobStore.useProperties";
public static final String PROP_DATASOURCE_PREFIX = "org.quartz.dataSource";
public static final String PROP_CONNECTION_PROVIDER_CLASS = "connectionProvider.class";
public static final String PROP_DATASOURCE_DRIVER = "driver";
public static final String PROP_DATASOURCE_URL = "URL";
public static final String PROP_DATASOURCE_USER = "user";
public static final String PROP_DATASOURCE_PASSWORD = "password";
public static final String PROP_DATASOURCE_MAX_CONNECTIONS = "maxConnections";
public static final String PROP_DATASOURCE_VALIDATION_QUERY = "validationQuery";
public static final String PROP_DATASOURCE_JNDI_URL = "jndiURL";
public static final String PROP_DATASOURCE_JNDI_ALWAYS_LOOKUP = "jndiAlwaysLookup";
public static final String PROP_DATASOURCE_JNDI_INITIAL = "java.naming.factory.initial";
public static final String PROP_DATASOURCE_JNDI_PROVDER = "java.naming.provider.url";
public static final String PROP_DATASOURCE_JNDI_PRINCIPAL = "java.naming.security.principal";
public static final String PROP_DATASOURCE_JNDI_CREDENTIALS = "java.naming.security.credentials";
public static final String PROP_PLUGIN_PREFIX = "org.quartz.plugin";
public static final String PROP_PLUGIN_CLASS = "class";
public static final String PROP_JOB_LISTENER_PREFIX = "org.quartz.jobListener";
public static final String PROP_TRIGGER_LISTENER_PREFIX = "org.quartz.triggerListener";
public static final String PROP_LISTENER_CLASS = "class";
public static final String DEFAULT_INSTANCE_ID = "NON_CLUSTERED";
public static final String AUTO_GENERATE_INSTANCE_ID = "AUTO";
/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Data members.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
private SchedulerException initException = null;
private String propSrc = null;
private PropertiesParser cfg;
private final Logger log = LoggerFactory.getLogger(getClass());
}
