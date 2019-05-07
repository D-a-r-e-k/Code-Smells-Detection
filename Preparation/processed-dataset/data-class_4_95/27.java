/** 
	 * Sets message to be displayed when maximum allowed login 
	 * attempts has reached.
	 * Default is : -ERR Max Auth Try Reached
	 * @see #getMaxAuthTryMsg
	 */
public void setMaxAuthTryMsg(String msg) {
    maxAuthTryMsg = msg;
    logger.finest("Set to " + msg);
}
