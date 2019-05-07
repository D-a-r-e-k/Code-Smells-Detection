/**
	 * @see org.columba.mail.folder.FolderTreeNode#removeFolder()
	 */
public void removeFolder() throws Exception {
    try {
        if (existsOnServer) {
            String path = getImapPath();
            getServer().deleteFolder(path);
        }
        super.removeFolder();
    } catch (Exception e) {
        throw e;
    }
}
