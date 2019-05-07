/**
   * Returns the logger for this Folder.
   */
public Logger getLogger() {
    if (mLogger == null) {
        mLogger = Logger.getLogger(getFolderProperty());
    }
    return mLogger;
}
