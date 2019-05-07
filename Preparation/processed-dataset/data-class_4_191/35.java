/**
   * This just checks to see if we can get a NewMessageCount from the
   * folder.  As a brute force method, it also accesses the folder
   * at every check.  It's nasty, but it _should_ keep the Folder open..
   */
public void checkFolder() throws javax.mail.MessagingException, OperationCancelledException {
    folderLog(Level.FINE, "checking folder " + getFolderID());
    // i'm taking this almost directly from ICEMail; i don't know how 
    // to keep the stores/folders open, either.  :) 
    if (isConnected()) {
        Folder current = getFolder();
        if (current != null && current.isOpen()) {
            current.getNewMessageCount();
            current.getUnreadMessageCount();
        }
        resetMessageCounts();
    }
}
