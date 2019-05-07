/**
     * Sets the ClientCommandHandler class that interacts with 
	 * client sockets.
	 * @param handler the fully qualified name of the class that 
	 *  implements {@link ClientCommandHandler}
	 * @see #getClientCommandHandler
     */
public void setClientCommandHandler(String handler) {
    clientCommandHandlerString = handler;
    logger.finest("Set to " + handler);
}
