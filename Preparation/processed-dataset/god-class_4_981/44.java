/**
     * <p>
     * Called by the QuartzScheduler before the <code>JobStore</code> is
     * used, in order to give it a chance to initialize.
     * </p>
     */
public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
    if (dsName == null) {
        throw new SchedulerConfigException("DataSource name not set.");
    }
    classLoadHelper = loadHelper;
    if (isThreadsInheritInitializersClassLoadContext()) {
        log.info("JDBCJobStore threads will inherit ContextClassLoader of thread: " + Thread.currentThread().getName());
        initializersLoader = Thread.currentThread().getContextClassLoader();
    }
    this.schedSignaler = signaler;
    // If the user hasn't specified an explicit lock handler, then  
    // choose one based on CMT/Clustered/UseDBLocks. 
    if (getLockHandler() == null) {
        // If the user hasn't specified an explicit lock handler,  
        // then we *must* use DB locks with clustering 
        if (isClustered()) {
            setUseDBLocks(true);
        }
        if (getUseDBLocks()) {
            getLog().info("Using db table-based data access locking (synchronization).");
            setLockHandler(new StdRowLockSemaphore(getTablePrefix(), getSelectWithLockSQL()));
        } else {
            getLog().info("Using thread monitor-based data access locking (synchronization).");
            setLockHandler(new SimpleSemaphore());
        }
    }
    if (!isClustered()) {
        try {
            cleanVolatileTriggerAndJobs();
        } catch (SchedulerException se) {
            throw new SchedulerConfigException("Failure occured during job recovery.", se);
        }
    }
}
