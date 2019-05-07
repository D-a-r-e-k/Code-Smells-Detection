/**
	 * Copies a set of messages from this folder to a destination folder.
	 * <p>
	 * The IMAP copy command also keeps the flags intact. So, there's no need to
	 * change these manually.
	 * 
	 * @see org.columba.mail.folder.Folder#innerCopy(org.columba.mail.folder.IMailbox,
	 *      java.lang.Object, org.columba.api.command.IWorkerStatusController)
	 */
public void innerCopy(IMailbox destiny, Object[] uids) throws Exception {
    IMAPFolder destFolder = (IMAPFolder) destiny;
    IHeaderList srcHeaderList = getHeaderList();
    IHeaderList destHeaderList = destFolder.getHeaderList();
    Object[] destUids = getServer().copy(destFolder, uids, this);
    if (destUids.length != uids.length) {
        LOG.warning("Some messages could not be copied because they do not exist anymore!");
    }
    // update headerlist of destination-folder 
    // -> this is necessary to reflect the changes visually 
    int j = 0;
    for (int i = 0; i < uids.length; i++) {
        IColumbaHeader destHeader = (IColumbaHeader) srcHeaderList.get(uids[i]).clone();
        // Was this message actually copied? 
        destHeader.set("columba.uid", destUids[j]);
        destHeaderList.add(destHeader, destUids[j]);
        // We need IMAPFlags 
        IMAPFlags flags = new IMAPFlags(destHeader.getFlags().getFlags());
        flags.setUid(destUids[j]);
        destFolder.fireMessageAdded(flags);
        j++;
    }
}
