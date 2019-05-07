/**
	 * Returns {@link QSAdminServer} associated with this QuickServer
	 * @since 1.1
	 */
public QSAdminServer getQSAdminServer() {
    if (adminServer == null)
        adminServer = new QSAdminServer(QuickServer.this);
    return adminServer;
}
