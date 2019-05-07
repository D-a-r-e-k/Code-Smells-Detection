/**
   * Returns the logger for this Store.
   */
public Logger getLogger() {
    if (mLogger == null) {
        mLogger = Logger.getLogger(getStoreProperty());
    }
    return mLogger;
}
