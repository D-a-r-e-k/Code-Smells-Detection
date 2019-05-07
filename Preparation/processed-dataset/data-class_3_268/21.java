/**
	 * @see org.columba.mail.folder.Folder#expungeFolder(java.lang.Object,
	 *      org.columba.api.command.IWorkerStatusController)
	 */
public void expungeFolder() throws Exception {
    try {
        getServer().expunge(this);
        super.expungeFolder();
    } catch (Exception e) {
        throw e;
    }
}
