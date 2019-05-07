/**
	 * @see org.columba.mail.folder.Folder#markMessage(java.lang.Object, int,
	 *      IMAPFolder)
	 */
public void markMessage(Object[] uids, int variant) throws Exception {
    getServer().markMessage(uids, variant, this);
    super.markMessage(uids, variant);
}
