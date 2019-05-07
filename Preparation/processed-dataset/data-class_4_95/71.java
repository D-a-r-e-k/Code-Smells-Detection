/**
	 * Initialise and create the service.
	 * @param qsConfig QuickServerConfig object.
	 * @since 1.4.7
	 */
public synchronized void initServer(QuickServerConfig qsConfig) throws AppException {
    setConfig(qsConfig);
    try {
        configQuickServer();
        loadApplicationClasses();
        //start InitServerHooks  
        InitServerHooks ish = getConfig().getInitServerHooks();
        if (ish != null) {
            Iterator iterator = ish.iterator();
            String initServerHookClassName = null;
            Class initServerHookClass = null;
            InitServerHook initServerHook = null;
            while (iterator.hasNext()) {
                initServerHookClassName = (String) iterator.next();
                initServerHookClass = getClass(initServerHookClassName, true);
                initServerHook = (InitServerHook) initServerHookClass.newInstance();
                logger.info("Loaded init server hook: " + initServerHookClassName);
                logger.fine("Init server hook info: " + initServerHook.info());
                initServerHook.handleInit(QuickServer.this);
            }
        }
    } catch (Exception e) {
        logger.severe("Could not load init server hook: " + e);
        logger.warning("StackTrace:\n" + MyString.getStackTrace(e));
        throw new AppException("Could not load init server hook", e);
    }
    setServiceState(Service.INIT);
    logger.finest("\r\n" + MyString.getSystemInfo(getVersion()));
}
