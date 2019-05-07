/**
     * Sets the ClientWriteHandler class that interacts with 
	 * client sockets to handle data write (only used in non-blocking mode).
	 * @param handler object the fully qualified name of the class that 
	 *  implements {@link ClientWriteHandler}
	 * @see #getClientWriteHandler
	 * @since 1.4.5
     */
public void setClientWriteHandler(String handler) {
    clientWriteHandlerString = handler;
    logger.finest("Set to " + handler);
}
