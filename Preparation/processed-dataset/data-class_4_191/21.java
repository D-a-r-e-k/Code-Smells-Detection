/**
   * During loadAllMessages, updates the display to say that we're loading
   * messages.
   */
protected void updateDisplay(boolean start) {
    if (getFolderDisplayUI() != null) {
        if (start) {
            getFolderDisplayUI().setBusy(true);
            getFolderDisplayUI().showStatusMessage(Pooka.getProperty("messages.Folder.loading.starting", "Loading messages."));
        } else {
            getFolderDisplayUI().setBusy(false);
            getFolderDisplayUI().showStatusMessage(Pooka.getProperty("messages.Folder.loading.finished", "Done loading messages."));
        }
    }
}
