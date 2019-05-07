/**
   * This closes the current Folder as well as all subfolders.
   */
public void closeAllFolders(boolean expunge, boolean shuttingDown) throws MessagingException {
    /*
      if (shuttingDown && loaderThread != null) {
      loaderThread.stopThread();
      }
    */
    if (shuttingDown && mMessageLoader != null) {
        mMessageLoader.stopLoading();
    }
    synchronized (getFolderThread().getRunLock()) {
        MessagingException otherException = null;
        Vector folders = getChildren();
        if (folders != null) {
            for (int i = 0; i < folders.size(); i++) {
                try {
                    ((FolderInfo) folders.elementAt(i)).closeAllFolders(expunge, shuttingDown);
                } catch (MessagingException me) {
                    if (otherException == null)
                        otherException = me;
                } catch (Exception e) {
                    MessagingException newMe = new MessagingException(e.getMessage(), e);
                    if (otherException == null)
                        otherException = newMe;
                }
            }
        }
        closeFolder(expunge, false);
        if (otherException != null)
            throw otherException;
    }
}
