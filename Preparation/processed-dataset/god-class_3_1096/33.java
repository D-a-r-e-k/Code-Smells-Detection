/**
	 * 
	 * 
	 * @see org.columba.mail.folder.IMailbox#getMimePartBodyStream(java.lang.Object,
	 *      java.lang.Integer[])
	 */
public InputStream getMimePartBodyStream(Object uid, Integer[] address) throws Exception {
    InputStream result = IMAPCache.getInstance().getMimeBody(this, uid, address);
    if (result == null) {
        LOG.fine("Cache miss - fetching from server");
        result = IMAPCache.getInstance().addMimeBody(this, uid, address, getServer().getMimePartBodyStream(uid, address, this));
    }
    return result;
}
