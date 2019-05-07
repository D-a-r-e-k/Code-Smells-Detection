/**
     * Sets the ClientExtendedEventHandler objects class that gets notified of 
	 * extended client events.
	 * @param handler object that 
	 *  implements {@link ClientExtendedEventHandler}
	 * @see #getClientExtendedEventHandler
	 * @since 1.4.6
     */
public void setClientExtendedEventHandler(ClientExtendedEventHandler handler) {
    this.extendedEventHandler = handler;
}
