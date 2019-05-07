/**
     * Returns the Authenticator class that 
	 * handles the authentication of a client.
	 * @see #setAuthenticator
	 * @deprecated since 1.4.6 use getClientAuthenticationHandler
	 * @since 1.3
     */
public String getAuthenticator() {
    return clientAuthenticationHandlerString;
}
