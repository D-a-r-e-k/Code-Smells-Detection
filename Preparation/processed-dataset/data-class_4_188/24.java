/**
   * This appends the given message to the given FolderInfo.
   */
public void appendMessages(MessageInfo[] msgs) throws MessagingException, OperationCancelledException {
    if (isAvailable()) {
        if (isConnected()) {
            super.appendMessages(msgs);
        } else {
            // make sure we've loaded 
            if (!isLoaded())
                loadFolder();
            getCache().appendMessages(msgs);
        }
    } else {
        throw new MessagingException("cannot append messages to an unavailable folder.");
    }
}
