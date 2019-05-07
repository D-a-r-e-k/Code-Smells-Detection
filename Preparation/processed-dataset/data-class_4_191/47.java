/**
   * This appends the given message to the given FolderInfo.
   */
public void appendMessages(MessageInfo[] msgs) throws MessagingException, OperationCancelledException {
    if (!isSortaOpen())
        openFolder(Folder.READ_WRITE);
    Message[] m = new Message[msgs.length];
    for (int i = 0; i < msgs.length; i++) {
        m[i] = msgs[i].getRealMessage();
    }
    getFolder().appendMessages(m);
}
