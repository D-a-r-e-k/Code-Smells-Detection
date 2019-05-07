/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMimePartSourceStream(java.lang.Object,
	 *      java.lang.Integer[])
	 */
public InputStream getMimePartSourceStream(Object uid, Integer[] address) throws Exception {
    return getServer().getMimePartSourceStream(uid, address, this);
}
