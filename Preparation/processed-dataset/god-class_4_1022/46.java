/**
   * This copies the given messages to the given FolderInfo.
   */
public void copyMessages(MessageInfo[] msgs, FolderInfo targetFolder) throws MessagingException, OperationCancelledException {
    if (targetFolder == null) {
        throw new MessagingException(Pooka.getProperty("error.null", "Error: null folder"));
    } else if (targetFolder.getStatus() == INVALID) {
        throw new MessagingException(Pooka.getProperty("error.folderInvalid", "Error:  folder is invalid.  ") + targetFolder.getFolderID());
    }
    if (!targetFolder.isAvailable())
        targetFolder.loadFolder();
    synchronized (targetFolder.getFolderThread().getRunLock()) {
        Folder target = targetFolder.getFolder();
        if (target != null) {
            Message[] m = new Message[msgs.length];
            for (int i = 0; i < msgs.length; i++) {
                m[i] = msgs[i].getRealMessage();
            }
            getFolder().copyMessages(m, target);
            // if we do a copy, we'll probably need to do a refresh on the target 
            // folder, also. 
            targetFolder.checkFolder();
        } else {
            targetFolder.appendMessages(msgs);
        }
    }
}
