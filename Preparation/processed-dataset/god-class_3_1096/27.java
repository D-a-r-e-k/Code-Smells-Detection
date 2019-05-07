/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.FolderTreeNode#addSubfolder(org.columba.mail.folder.FolderTreeNode)
	 */
public void addSubfolder(IMailFolder child) throws Exception {
    if (child instanceof IMAPFolder) {
        getServer().createMailbox(child.getName(), this);
    }
    super.addSubfolder(child);
}
