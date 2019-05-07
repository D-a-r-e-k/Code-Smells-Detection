/**
     * Sets the ClientAuthenticationHandler class that 
	 * handles the authentication of a client.
	 * @param authenticator the fully qualified name of the class 
	 * that implements {@link ClientAuthenticationHandler}.
	 * @see #getClientAuthenticationHandler
	 * @since 1.4.6
     */
public void setClientAuthenticationHandler(String authenticator) {
    clientAuthenticationHandlerString = authenticator;
    logger.finest("Set to " + authenticator);
}
