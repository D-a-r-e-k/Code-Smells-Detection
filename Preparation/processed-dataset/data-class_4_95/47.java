/**
	 * Starts QSAdminServer for this QuickServer.
	 * @see org.quickserver.net.qsadmin.QSAdminServer
	 * @since 1.2
	 */
public void startQSAdminServer() throws AppException {
    getQSAdminServer().startServer();
}
