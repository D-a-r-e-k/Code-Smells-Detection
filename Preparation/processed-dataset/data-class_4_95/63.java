/**
	 * Initialise and create the service.
	 * @param param of the xml configuration file.
	 * @since 1.2
	 */
public synchronized boolean initService(Object param[]) {
    serviceError = null;
    try {
        initServer(param);
    } catch (Exception e) {
        serviceError = e;
        return false;
    }
    return true;
}
