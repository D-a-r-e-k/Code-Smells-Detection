/**
	 * @see org.columba.mail.folder.FolderTreeNode#releaseLock()
	 */
public void releaseLock(Object locker) {
    IMailFolder root = getRootFolder();
    if (root == null)
        throw new IllegalArgumentException("IMAPRoot is null");
    root.releaseLock(locker);
}
