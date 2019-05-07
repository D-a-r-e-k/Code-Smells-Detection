/**
   * While loading messages, attempts to update the folder status.
   */
protected void updateFolderStatusForLoading() throws MessagingException, OperationCancelledException {
    if (!isConnected()) {
        openFolder(Folder.READ_WRITE);
    }
}
