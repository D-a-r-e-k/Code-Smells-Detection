private void startThreads() {
    try {
        UserManager.startUserManager();
        RequestReader.startRequestReader(true);
        RequestReader.startRequestReader(true);
        CentralSelector.startCentralSelector();
        Responder.startResponder();
        Listener.startListener();
        LogCleaner.startLogCleaner();
    } catch (Exception e) {
        Server.debug(this, "Exception during starting threads:", e, MSG_ERROR, LVL_HALT);
    }
    try {
        TrafficMonitor.startTrafficMonitor();
    } catch (Exception e) {
        Server.debug(this, "Exception during starting TrafficMonitor: ", e, MSG_ERROR, LVL_MAJOR);
    }
    try {
        XmlRpcManager.startManager();
    } catch (Exception e) {
        Server.debug(this, "Exception during starting XmlRpcManager (Server will not be reachable via XML-RPC):", e, MSG_ERROR, LVL_MAJOR);
    }
}
