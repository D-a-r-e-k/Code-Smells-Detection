/**
     * Returns the Authenticator object that handles the 
	 * authentication of a client.
	 * @see #setAuthenticator
	 * @since 1.3
	 * @deprecated As of 1.4.6 use {@link #getClientAuthenticationHandler}
     */
public Authenticator getAuthenticator() {
    return authenticator;
}
