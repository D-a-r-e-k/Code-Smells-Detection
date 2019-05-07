/**
   * Execute the precommand if there is one.
   */
private void executePrecommand() {
    String preCommand = Pooka.getProperty(getStoreProperty() + ".precommand", "");
    if (preCommand.length() > 0) {
        getLogger().log(Level.FINE, "connect store " + getStoreID() + ":  executing precommand.");
        try {
            Process p = Runtime.getRuntime().exec(preCommand);
            p.waitFor();
        } catch (Exception ex) {
            getLogger().log(Level.FINE, "Could not run precommand:");
            ex.printStackTrace();
        }
    }
}
