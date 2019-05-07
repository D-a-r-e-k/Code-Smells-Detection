/**
   * Updates the debug status on the session.
   */
void updateSessionDebug() {
    if (Pooka.getProperty("Pooka.sessionDebug", "false").equalsIgnoreCase("true") || (!Pooka.getProperty(getStoreProperty() + ".sessionDebug.logLevel", "OFF").equalsIgnoreCase("OFF"))) {
        mSession.setDebug(true);
    } else {
        mSession.setDebug(false);
    }
}
