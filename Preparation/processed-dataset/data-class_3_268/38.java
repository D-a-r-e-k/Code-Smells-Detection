/**
	 * @see org.columba.mail.folder.IMailbox#getAllHeaderFields(java.lang.Object)
	 */
public Header getAllHeaderFields(Object uid) throws Exception {
    return getServer().getAllHeaders(uid, this);
}
