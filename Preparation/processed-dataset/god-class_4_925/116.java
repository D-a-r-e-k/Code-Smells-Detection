/**
	 * Returns ServerHooks if present else <code>null</code>.
	 * @since 1.3.3
	 */
public ServerHooks getServerHooks() {
    if (serverHooks == null)
        serverHooks = new ServerHooks();
    return serverHooks;
}
