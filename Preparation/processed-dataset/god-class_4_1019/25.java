/**
   * This expunges the deleted messages from the Folder.
   */
public void expunge() throws MessagingException, OperationCancelledException {
    if (isConnected())
        getFolder().expunge();
    else if (shouldBeConnected()) {
        openFolder(Folder.READ_WRITE);
        getFolder().expunge();
    } else {
        getCache().expungeMessages();
    }
}
