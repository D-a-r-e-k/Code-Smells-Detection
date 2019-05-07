/**
	 * @param lazy TODO
	 * @param forceSelect TODO
	 * @throws IOException
	 * @throws IMAPException
	 * @throws CommandCancelledException
	 * @throws Exception
	 */
public synchronized void ensureFolderIsSynced(boolean lazy, boolean forceSelect) throws IOException, IMAPException, CommandCancelledException, Exception {
    if (forceSelect) {
        getServer().ensureSelectedState(this);
    }
    if (!headerList.isRestored()) {
        try {
            headerList.restore();
        } catch (IOException e) {
        }
        synchronizeHeaderlist();
        // Only do a flag sync if this folder is selected 
        // -> this prevents an explicit select to this folder 
        // when only check for new messages 
        if (getServer().isSelected(this)) {
            synchronizeFlags();
        } else {
            // but we have to ensure that it happens later 
            lazyFlagSync = true;
        }
    } else if (!lazy || !getServer().isSelected(this)) {
        synchronizeHeaderlist();
        if (lazyFlagSync && getServer().isSelected(this)) {
            // One flag sync should be enough for one session 
            synchronizeFlags();
            lazyFlagSync = false;
        }
    }
}
