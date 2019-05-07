/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#addMessage(java.io.InputStream,
	 *      org.columba.ristretto.message.Attributes)
	 */
public Object addMessage(InputStream in) throws Exception {
    PassiveHeaderParserInputStream withHeaderInputStream = new PassiveHeaderParserInputStream(in);
    Integer uid = getServer().append(withHeaderInputStream, this);
    // update the HeaderList 
    Header header = withHeaderInputStream.getHeader();
    ColumbaHeader h = new ColumbaHeader(header);
    headerList.add(h, uid);
    fireMessageAdded(uid);
    return uid;
}
