/**
   * Actually records that the folder has been opened or closed.
   * This is separated out so that subclasses can override it more
   * easily.
   */
protected void updateFolderOpenStatus(boolean isNowOpen) {
    if (isNowOpen) {
        setStatus(CONNECTED);
    } else {
        setStatus(CLOSED);
    }
}
