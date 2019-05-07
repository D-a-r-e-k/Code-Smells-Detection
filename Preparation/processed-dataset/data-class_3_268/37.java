/**
	 * @see org.columba.mail.folder.Folder#getHeaderListStorage()
	 */
/*
	public IHeaderListStorage getHeaderListStorage() {
		if (attributeStorage == null)
			attributeStorage = new RemoteHeaderListStorage(this);

		return attributeStorage;
	}*/
/**
	 * @see org.columba.mail.folder.Folder#getSearchEngineInstance()
	 */
public DefaultSearchEngine getSearchEngine() {
    if (searchEngine == null) {
        searchEngine = new DefaultSearchEngine(this);
        searchEngine.setNonDefaultEngine(new IMAPQueryEngine(this));
    }
    return searchEngine;
}
