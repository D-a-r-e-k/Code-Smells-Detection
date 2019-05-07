/**
   * Logs a message for this folder.
   */
public void folderLog(Level l, String message) {
    getLogger().log(l, getFolderID() + ":  " + message);
}
