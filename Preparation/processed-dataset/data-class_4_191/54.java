/**
   * This creates a folder if it doesn't exist already.  If it does exist,
   * but is not of the right type, or if there is a problem in creating the
   * folder, throws an error.
   */
public void createSubFolder(String subFolderName, int type) throws MessagingException, OperationCancelledException {
    if (!isLoaded()) {
        loadFolder();
    }
    if (folder != null) {
        Folder subFolder = folder.getFolder(subFolderName);
        if (subFolder == null) {
            throw new MessagingException("Store returned null for subfolder " + subFolderName + " of folder " + getFolderName());
        }
        if (!subFolder.exists())
            subFolder.create(type);
        subscribeFolder(subFolderName);
    } else {
        throw new MessagingException("Failed to open folder " + getFolderName() + " to create subfolder " + subFolderName);
    }
}
