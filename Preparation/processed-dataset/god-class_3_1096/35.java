/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.Folder#isTrashFolder()
	 */
public boolean isTrashFolder() {
    RootFolder root = (RootFolder) getRootFolder();
    if (root != null) {
        return root.getTrashFolder() == this;
    } else {
        return false;
    }
}
