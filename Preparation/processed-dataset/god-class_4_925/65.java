/**
	 * Start the service.
	 * @return true if serivce was stopped from Running state.
	 * @since 1.2
	 */
public boolean startService() {
    serviceError = null;
    if (getServiceState() == Service.RUNNING)
        return false;
    try {
        startServer();
    } catch (AppException e) {
        serviceError = e;
        return false;
    }
    return true;
}
