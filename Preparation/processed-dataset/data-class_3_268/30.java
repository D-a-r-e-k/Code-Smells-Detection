/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.IMailbox#getHeaderFields(java.lang.Object,
	 *      java.lang.String[])
	 */
public Header getHeaderFields(Object uid, String[] keys) throws Exception {
    // get header with UID 
    IColumbaHeader header = getHeaderList().get(uid);
    if (header == null)
        return new Header();
    // cached headerfield list 
    List cachedList = Arrays.asList(CachedHeaderfields.getDefaultHeaderfields());
    List keyList = new ArrayList(Arrays.asList(keys));
    ListTools.substract(keyList, cachedList);
    if (keyList.size() > 0) {
        return getServer().getHeaders(uid, keys, this);
    } else {
        return (Header) header.getHeader().clone();
    }
}
