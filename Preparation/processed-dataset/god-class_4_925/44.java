/** 
	 * Set the ClientAuthenticationHandler class of 
	 * QSAdminServer that handles the authentication of a client.
	 * @since 1.2
	 */
public void setQSAdminServerAuthenticator(String authenticator) {
    getQSAdminServer().getServer().setClientAuthenticationHandler(authenticator);
}
