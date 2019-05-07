/**
	 * Suspends the service.
	 * @return true if service was suspended from resumed state.
	 * @since 1.2
	 */
public boolean suspendService() {
    serviceError = null;
    if (getServiceState() == Service.RUNNING) {
        suspendMaxConnection = maxConnection;
        suspendMaxConnectionMsg = maxConnectionMsg;
        maxConnection = 0;
        maxConnectionMsg = "Service is suspended.";
        setServiceState(Service.SUSPENDED);
        logger.info("Service " + getName() + " is suspended.");
        return true;
    }
    return false;
}
