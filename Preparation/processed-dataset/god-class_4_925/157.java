/**
     * Sets the ClientExtendedEventHandler class that gets notified of 
	 * extended client events.
	 * @param handler the fully qualified name of the class that 
	 *  implements {@link ClientExtendedEventHandler}
	 * @see #getClientExtendedEventHandler
	 * @since 1.4.6
     */
public void setClientExtendedEventHandler(String handler) {
    clientExtendedEventHandlerString = handler;
    logger.finest("Set to " + handler);
}
