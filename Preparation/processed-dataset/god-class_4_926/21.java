/**
     * Sets the ClientCommandHandler objects that interacts with 
	 * client sockets.
	 * @param handler object that 
	 *  implements {@link ClientCommandHandler}
	 * @see #getClientCommandHandler
     */
public void setClientCommandHandler(ClientCommandHandler handler) {
    this.commandHandler = handler;
}
