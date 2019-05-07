/**
     * Sets the ClientEventHandler class that gets notified of 
	 * client events.
	 * @param handler the fully qualified name of the class that 
	 *  implements {@link ClientEventHandler}
	 * @see #getClientEventHandler
	 * @since 1.4.6
     */
public void setClientEventHandler(String handler) {
    clientEventHandlerString = handler;
    logger.finest("Set to " + handler);
}
