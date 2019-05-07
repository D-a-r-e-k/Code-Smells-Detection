/**
     * Sets the Authenticator class that 
	 * handles the authentication of a client.
	 * @param authenticator the fully qualified name of the class 
	 * that implements {@link Authenticator} or {@link ClientAuthenticationHandler}.
	 * @see #getAuthenticator
	 * @deprecated since 1.4.6 use setClientAuthenticationHandler
	 * @since 1.3
     */
public void setAuthenticator(String authenticator) {
    clientAuthenticationHandlerString = authenticator;
    logger.finest("Set to " + authenticator);
}
