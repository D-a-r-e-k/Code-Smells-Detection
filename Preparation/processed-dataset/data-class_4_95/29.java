/**
	 * Sets timeout message. 
	 * Default is : -ERR Timeout
	 * @see #getTimeoutMsg
	 */
public void setTimeoutMsg(String msg) {
    timeoutMsg = msg;
    logger.finest("Set to " + msg);
}
