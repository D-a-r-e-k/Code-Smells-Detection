/**
	 * Starts QSAdminServer for this QuickServer.
	 * @see org.quickserver.net.qsadmin.QSAdminServer
	 * @param authenticator sets the ClientAuthenticationHandler class that 
	 *   handles the authentication of a client, 
	 *   if null uses {@link org.quickserver.net.qsadmin.Authenticator}.
	 * @param port to run QSAdminServer on
 	 * @exception org.quickserver.net.AppException 
	 *  if Server already running or if it could not load the classes
	 *  [ClientCommandHandler, ClientAuthenticationHandler, ClientData].
	 * @since 1.1
	 */
public void startQSAdminServer(int port, String authenticator) throws AppException {
    getQSAdminServer().setClientAuthenticationHandler(authenticator);
    getQSAdminServer().startServer(port);
}
