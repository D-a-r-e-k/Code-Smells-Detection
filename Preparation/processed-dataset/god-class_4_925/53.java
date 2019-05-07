/**
     * Sets the ClientObjectHandler class that interacts with 
	 * client sockets to handle java objects.
	 * @param handler object the fully qualified name of the class that 
	 *  implements {@link ClientObjectHandler}
	 * @see #getClientObjectHandler
	 * @since 1.2
     */
public void setClientObjectHandler(String handler) {
    clientObjectHandlerString = handler;
    logger.finest("Set to " + handler);
}
