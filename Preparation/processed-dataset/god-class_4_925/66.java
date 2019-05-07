/**
	 * Stop the service.
	 * @return true if serivce was stopped from Running state.
	 * @since 1.2
	 */
public boolean stopService() {
    serviceError = null;
    if (getServiceState() == Service.STOPPED)
        return false;
    try {
        stopServer();
        clearAllPools();
    } catch (AppException e) {
        serviceError = e;
        return false;
    } catch (Exception e) {
        serviceError = e;
        return false;
    }
    return true;
}
