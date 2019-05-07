/**
     * Sets the Authenticator class that handles the 
	 * authentication of a client.
	 * @param authenticator object that implements {@link Authenticator}.
	 * @see #getAuthenticator
	 * @since 1.3
	 * @deprecated As of 1.4.6 use {@link #setClientAuthenticationHandler}
     */
public void setAuthenticator(Authenticator authenticator) {
    this.authenticator = authenticator;
}
