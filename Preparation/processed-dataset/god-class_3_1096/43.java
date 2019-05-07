/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.AbstractMessageFolder#searchMessages(org.columba.core.filter.Filter)
	 */
public Object[] searchMessages(Filter filter) throws Exception {
    ensureFolderIsSynced(true, true);
    return super.searchMessages(filter);
}
