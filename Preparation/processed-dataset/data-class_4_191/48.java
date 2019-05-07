/**
   * This expunges the deleted messages from the Folder.
   */
public void expunge() throws MessagingException, OperationCancelledException {
    getFolder().expunge();
}
