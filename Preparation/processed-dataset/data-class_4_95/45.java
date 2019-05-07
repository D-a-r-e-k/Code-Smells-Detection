/** 
	 * Returns the Authenticator or ClientAuthenticationHandler class of 
	 * QSAdminServer that handles the authentication of a client.
	 * @since 1.2
	 */
public String getQSAdminServerAuthenticator() {
    return getQSAdminServer().getServer().getAuthenticator();
}
