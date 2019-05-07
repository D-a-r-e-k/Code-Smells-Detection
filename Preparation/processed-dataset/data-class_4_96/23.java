/**
     * Sets the ClientObjectHandler object that interacts with 
	 * client sockets.
	 * @param handler object that 
	 *  implements {@link ClientObjectHandler}
	 * @see #getClientObjectHandler
	 * @since 1.2
     */
public void setClientObjectHandler(ClientObjectHandler handler) {
    this.objectHandler = handler;
}
