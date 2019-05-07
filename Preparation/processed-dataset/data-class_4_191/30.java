/**
   * Unloads all messages.  This should be run if ever the current message
   * information becomes out of date, as can happen when the connection
   * to the folder goes down.
   */
public void unloadAllMessages() {
    folderTableModel = null;
}
