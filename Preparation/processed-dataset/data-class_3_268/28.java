/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.Folder#getObservable()
	 */
public IStatusObservable getObservable() {
    return ((IMAPRootFolder) getRootFolder()).getObservable();
}
