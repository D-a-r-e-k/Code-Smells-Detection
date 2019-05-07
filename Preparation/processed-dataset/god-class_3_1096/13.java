/**
	 * Propagates an event to all registered listeners notifying them of a
	 * message addition.
	 */
private void fireMessageAdded(IMAPFlags flags) {
    getMessageFolderInfo().incExists();
    try {
        if (flags.getRecent()) {
            getMessageFolderInfo().incRecent();
        }
        if (!flags.getSeen()) {
            getMessageFolderInfo().incUnseen();
        }
    } catch (Exception e) {
    }
    setChanged(true);
    // update treenode 
    fireFolderPropertyChanged();
    FolderEvent e = new FolderEvent(this, flags.getUid());
    // Guaranteed to return a non-null array 
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying 
    // those that are interested in this event 
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == IFolderListener.class) {
            ((IFolderListener) listeners[i + 1]).messageAdded(e);
        }
    }
}
