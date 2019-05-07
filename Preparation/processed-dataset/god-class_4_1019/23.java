/**
   * This copies the given messages to the given FolderInfo.
   */
public void copyMessages(MessageInfo[] msgs, FolderInfo targetFolder) throws MessagingException, OperationCancelledException {
    if (isConnected())
        super.copyMessages(msgs, targetFolder);
    else
        targetFolder.appendMessages(msgs);
}
