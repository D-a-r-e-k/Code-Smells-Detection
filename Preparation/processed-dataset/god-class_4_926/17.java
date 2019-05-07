/**
     * Sets the ClientEventHandler objects class that gets notified of 
	 * client events.
	 * @param handler object that 
	 *  implements {@link ClientEventHandler}
	 * @see #getClientEventHandler
	 * @since 1.4.6
     */
public void setClientEventHandler(ClientEventHandler handler) {
    this.eventHandler = handler;
}
