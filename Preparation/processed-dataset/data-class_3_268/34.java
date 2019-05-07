/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.Folder#isInboxFolder()
	 */
public boolean isInboxFolder() {
    RootFolder root = (RootFolder) getRootFolder();
    if (root != null) {
        return root.getInboxFolder() == this;
    } else {
        return false;
    }
}
