/**
	 * Sets the message to be sent to any new client connected after
	 * maximum client connection has reached. 
	 * Default is : <code>-ERR Server Busy. Max Connection Reached</code>
	 * @since 1.1
	 * @see #getMaxConnectionMsg
	 */
public void setMaxConnectionMsg(String maxConnectionMsg) {
    if (getServiceState() == Service.SUSPENDED)
        suspendMaxConnectionMsg = maxConnectionMsg;
    else
        this.maxConnectionMsg = maxConnectionMsg;
    logger.finest("Set to " + maxConnectionMsg);
}
