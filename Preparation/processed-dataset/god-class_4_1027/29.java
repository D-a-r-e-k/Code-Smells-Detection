public void initialize() {
    boolean interrupted = false;
    ServerConfiguration serverConfiguration = Server.getServer().getServerConfiguration();
    IContext context = this.getContext();
    if (context != null && context instanceof HostContext) {
        HostContext hostContext = (HostContext) context;
        Socket socket = hostContext.getSocket();
        if (!Tools.isLocal(socket.getInetAddress().getHostAddress()) && serverConfiguration.canShare()) {
            log.info("Remote access attempted from " + socket.getInetAddress().getHostAddress());
            if (getFactory() instanceof AppFactory) {
                AppConfiguration appConfiguration = (AppConfiguration) ((AppFactory) getFactory()).getAppContext().getConfiguration();
                if (appConfiguration.isShared()) {
                    push(new PinScreen(this), TRANSITION_NONE);
                    interrupted = true;
                }
            }
        }
    }
    if (!interrupted) {
        try {
            if (!Server.getServer().isCurrentVersion()) {
                push(new VersionScreen(this), TRANSITION_NONE);
            }
        } catch (Exception ex) {
            Tools.logException(DefaultApplication.class, ex);
        }
    }
}
