/**
	 * Loads the server from the xml file name passed.
	 * @since 1.4.7
	 */
public static QuickServer load(String xml) throws AppException {
    QuickServer qs = new QuickServer();
    Object config[] = new Object[] { xml };
    qs.initServer(config);
    qs.startServer();
    if (qs.getConfig().getQSAdminServerConfig() != null) {
        qs.startQSAdminServer();
    }
    return qs;
}
