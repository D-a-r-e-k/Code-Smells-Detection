/**
     * Sets the ClientBinaryHandler class that interacts with 
	 * client sockets to handle binary data.
	 * @param handler object the fully qualified name of the class that 
	 *  implements {@link ClientBinaryHandler}
	 * @see #getClientBinaryHandler
	 * @since 1.4
     */
public void setClientBinaryHandler(String handler) {
    clientBinaryHandlerString = handler;
    logger.finest("Set to " + handler);
}
