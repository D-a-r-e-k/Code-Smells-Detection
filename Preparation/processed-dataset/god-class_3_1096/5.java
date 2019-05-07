/**
	 * @see org.columba.mail.folder.Folder#getHeaderList(org.columba.api.command.IWorkerStatusController)
	 */
public IHeaderList getHeaderList() throws Exception {
    ensureFolderIsSynced(true, false);
    return headerList;
}
