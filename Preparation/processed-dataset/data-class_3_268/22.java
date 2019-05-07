/**
	 * @see org.columba.mail.folder.Folder#getMessageHeader(java.lang.Object,
	 *      org.columba.api.command.IWorkerStatusController)
	 * @TODO dont use deprecated method
	 */
public IColumbaHeader getMessageHeader(Object uid) throws Exception {
    return getHeaderList().get(uid);
}
