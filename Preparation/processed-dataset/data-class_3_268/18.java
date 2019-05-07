/**
	 * @see org.columba.mail.folder.Folder#getMimeTree(java.lang.Object,
	 *      IMAPFolder)
	 */
public MimeTree getMimePartTree(Object uid) throws Exception {
    return getServer().getMimeTree(uid, this);
}
