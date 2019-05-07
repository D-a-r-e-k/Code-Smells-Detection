/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#addMessage(java.io.InputStream)
	 */
public Object addMessage(InputStream in, Attributes attributes, Flags flags) throws Exception {
    PassiveHeaderParserInputStream withHeaderInputStream = new PassiveHeaderParserInputStream(in);
    IMAPFlags imapFlags = new IMAPFlags(flags.getFlags());
    Integer uid = getServer().append(withHeaderInputStream, imapFlags, this);
    // Since JUNK is a non-system Flag we have to set it with 
    // an addtitional STORE command 
    if (((Boolean) attributes.get("columba.spam")).booleanValue()) {
        imapFlags.set(IMAPFlags.JUNK, true);
        getServer().setFlags(new Object[] { uid }, imapFlags, this);
    }
    // Parser the header 
    Header header = withHeaderInputStream.getHeader();
    // update the HeaderList 
    IColumbaHeader cHeader = new ColumbaHeader(header, (Attributes) attributes.clone(), imapFlags);
    header.set("columba.uid", uid);
    headerList.add(cHeader, uid);
    fireMessageAdded(uid);
    return uid;
}
