/**
   * This deletes the underlying Folder.
   */
public void delete() throws MessagingException, OperationCancelledException {
    if (!isLoaded())
        loadFolder();
    Folder f = getFolder();
    if (f == null)
        throw new MessagingException("No folder.");
    unsubscribe();
    if (f.isOpen())
        f.close(true);
    f.delete(true);
}
