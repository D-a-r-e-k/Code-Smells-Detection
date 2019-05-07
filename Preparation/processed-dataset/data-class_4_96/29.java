/**
     * Sets the ClientBinaryHandler object that interacts with 
	 * client sockets.
	 * @param handler object that 
	 *  implements {@link ClientBinaryHandler}
	 * @see #getClientBinaryHandler
	 * @since 1.4
     */
public void setClientBinaryHandler(ClientBinaryHandler handler) {
    this.binaryHandler = handler;
}
