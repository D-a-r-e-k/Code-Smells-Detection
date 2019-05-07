/**
	 * Method getStore.
	 * 
	 * @return IMAPStore
	 */
public IMAPServer getServer() {
    if (store == null) {
        store = ((IMAPRootFolder) getRootFolder()).getServer();
    }
    return store;
}
