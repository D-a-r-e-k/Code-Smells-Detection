/**
   * Searches for messages in this folder which match the given
   * SearchTerm.
   *
   * Basically wraps the call to Folder.search(), and then wraps the
   * returned Message objects as MessageInfos.
   */
public MessageInfo[] search(javax.mail.search.SearchTerm term) throws MessagingException, OperationCancelledException {
    if (folderTableModel == null)
        loadAllMessages();
    Message[] matchingMessages = folder.search(term);
    MessageInfo returnValue[] = new MessageInfo[matchingMessages.length];
    for (int i = 0; i < matchingMessages.length; i++) {
        folderLog(Level.FINE, "match " + i + " = " + matchingMessages[i]);
        MessageInfo info = getMessageInfo(matchingMessages[i]);
        folderLog(Level.FINE, "messageInfo " + i + " = " + info);
        returnValue[i] = info;
    }
    folderLog(Level.FINE, "got " + returnValue.length + " results.");
    return returnValue;
}
