/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMessageSourceStream(java.lang.Object)
	 */
public InputStream getMessageSourceStream(Object uid) throws Exception {
    return getServer().getMessageSourceStream(uid, this);
}
