/**
	 * Resume the service.
	 * @return true if service was resumed from suspended state.
	 * @since 1.2
	 */
public boolean resumeService() {
    serviceError = null;
    if (getServiceState() == Service.SUSPENDED) {
        maxConnection = suspendMaxConnection;
        maxConnectionMsg = suspendMaxConnectionMsg;
        setServiceState(Service.RUNNING);
        logger.info("Service " + getName() + " resumed.");
        return true;
    }
    return false;
}
