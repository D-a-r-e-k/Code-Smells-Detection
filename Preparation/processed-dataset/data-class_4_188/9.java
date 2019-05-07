/**
   * While loading messages, attempts to update the folder status.
   */
protected void updateFolderStatusForLoading() throws MessagingException, OperationCancelledException {
    if (preferredStatus < DISCONNECTED && !(isConnected() && getParentStore().getConnection().getStatus() == NetworkConnection.CONNECTED)) {
        try {
            openFolder(Folder.READ_WRITE);
        } catch (MessagingException me) {
            uidValidity = cache.getUIDValidity();
            preferredStatus = DISCONNECTED;
        }
    }
}
