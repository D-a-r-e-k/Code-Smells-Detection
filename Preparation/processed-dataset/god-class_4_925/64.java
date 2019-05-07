/**
	 * Initialise and create the service.
	 * @param qsConfig QuickServerConfig object.
	 * @since 1.4.6
	 */
public synchronized boolean initService(QuickServerConfig qsConfig) {
    serviceError = null;
    try {
        initServer(qsConfig);
    } catch (Exception e) {
        serviceError = e;
        return false;
    }
    return true;
}
