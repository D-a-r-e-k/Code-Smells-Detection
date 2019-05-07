/**
   * During loadAllMessages, updates the display to say that we're loading
   * messages.
   */
protected void updateDisplay(boolean start) {
    if (getFolderDisplayUI() != null) {
        if (start) {
            getFolderDisplayUI().setBusy(true);
            showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("messages.CachingFolder.loading.starting", "Loading messages."));
        } else {
            getFolderDisplayUI().setBusy(false);
            showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("messages.CachingFolder.loading.finished", "Done loading messages."));
        }
    }
}
