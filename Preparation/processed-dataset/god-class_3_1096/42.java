/*
	class RemoteHeaderListStorage extends AbstractHeaderListStorage {

		protected AbstractHeaderCache headerCache;

		private IMAPFolder folder;

		public RemoteHeaderListStorage(IMAPFolder folder) {
			super();
			this.folder = folder;

		}

		public AbstractHeaderCache getHeaderCacheInstance() {
			if (headerCache == null)
				headerCache = new RemoteHeaderCache(folder);

			return headerCache;
		}

	}*/
/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.AbstractMessageFolder#searchMessages(org.columba.core.filter.Filter,
	 *      java.lang.Object[])
	 */
public Object[] searchMessages(Filter filter, Object[] uids) throws Exception {
    ensureFolderIsSynced(true, true);
    return super.searchMessages(filter, uids);
}
