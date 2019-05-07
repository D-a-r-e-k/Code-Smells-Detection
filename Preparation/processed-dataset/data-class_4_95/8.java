/**
	 * Restarts the QuickServer.
	 *
	 * @exception org.quickserver.net.AppException 
	 *  if could not stop server or if it could not start the server.
	 * @since 1.2
	 */
public void restartServer() throws AppException {
    stopServer();
    startServer();
}
