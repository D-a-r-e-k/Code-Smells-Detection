/**
	 * Configures QSAdminServer and QuickServer based on the 
	 * internal QuickServerConfig object.
	 * @since 1.3
	 */
public void configQuickServer() throws Exception {
    configQuickServer(getConfig());
    if (getConfig().getQSAdminServerConfig() != null) {
        configQuickServer(getConfig().getQSAdminServerConfig());
    }
}
