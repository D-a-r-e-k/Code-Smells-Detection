/**
     * Sets the ClientAuthenticationHandler class that handles the 
	 * authentication of a client.
	 * @param clientAuthenticationHandler object that implements {@link ClientAuthenticationHandler}.
	 * @see #getClientAuthenticationHandler
	 * @since 1.4.6
     */
public void setClientAuthenticationHandler(ClientAuthenticationHandler clientAuthenticationHandler) {
    this.clientAuthenticationHandler = clientAuthenticationHandler;
}
