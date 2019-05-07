/**
	 * @see org.columba.mail.folder.FolderTreeNode#tryToGetLock(java.lang.Object)
	 */
public boolean tryToGetLock(Object locker) {
    // IMAP Folders have no own lock ,but share the lock from the Root 
    // to ensure that only one operation can be processed simultanous 
    IMailFolder root = getRootFolder();
    if (root == null)
        throw new IllegalArgumentException("IMAPRoot is null");
    return root.tryToGetLock(locker);
}
