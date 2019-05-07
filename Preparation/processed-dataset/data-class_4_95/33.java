//end of run  
/**
	 * Sets the maximum number of client connection allowed.
	 * @since 1.1
	 * @see #getMaxConnection
	 */
public void setMaxConnection(long maxConnection) {
    if (getServiceState() == Service.SUSPENDED)
        suspendMaxConnection = maxConnection;
    else
        this.maxConnection = maxConnection;
    logger.finest("Set to " + maxConnection);
}
