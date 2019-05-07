/**
   * This method opens the Folder, and sets the FolderInfo to know that
   * the Folder should be open.  You should use this method instead of
   * calling getFolder().open(), because if you use this method, then
   * the FolderInfo will try to keep the Folder open, and will try to
   * reopen the Folder if it gets closed before closeFolder is called.
   *
   * This method can also be used to reset the mode of an already
   * opened folder.
   */
public void openFolder(int mode) throws MessagingException, OperationCancelledException {
    openFolder(mode, true);
}
