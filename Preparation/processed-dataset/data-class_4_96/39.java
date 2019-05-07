/**
     * Sets the ClientWriteHandler object that interacts with 
	 * client sockets.
	 * @param handler object that 
	 *  implements {@link ClientWriteHandler}
	 * @see #getClientWriteHandler
	 * @since 1.4.5
     */
public void setClientWriteHandler(ClientWriteHandler handler) {
    this.writeHandler = handler;
}
