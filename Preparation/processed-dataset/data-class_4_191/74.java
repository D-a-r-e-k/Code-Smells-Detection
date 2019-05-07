/**
   * This does the real work when messages are removed.  This can be
   * overridden by subclasses.
   */
protected void runMessagesRemoved(MessageCountEvent mce) {
    folderLog(Level.FINE, "running MessagesRemoved on " + getFolderID());
    if (folderTableModel != null) {
        Message[] removedMessages = mce.getMessages();
        folderLog(Level.FINE, "removedMessages was of size " + removedMessages.length);
        MessageInfo mi;
        Vector removedProxies = new Vector();
        if (getLogger().isLoggable(Level.FINE)) {
            folderLog(Level.FINE, "message in info table:");
            Iterator<Message> keyIter = messageToInfoTable.keySet().iterator();
            while (keyIter.hasNext()) {
                folderLog(Level.FINE, keyIter.next().toString());
            }
        }
        for (int i = 0; i < removedMessages.length; i++) {
            folderLog(Level.FINE, "checking for existence of message " + removedMessages[i]);
            mi = getMessageInfo(removedMessages[i]);
            if (mi != null) {
                if (mi.getMessageProxy() != null)
                    mi.getMessageProxy().close();
                folderLog(Level.FINE, "message exists--removing");
                removedProxies.add(mi.getMessageProxy());
                messageToInfoTable.remove(removedMessages[i]);
            }
        }
        if (getFolderDisplayUI() != null) {
            if (removedProxies.size() > 0)
                getFolderDisplayUI().removeRows(removedProxies);
            resetMessageCounts();
            fireMessageCountEvent(mce);
        } else {
            resetMessageCounts();
            fireMessageCountEvent(mce);
            if (removedProxies.size() > 0)
                getFolderTableModel().removeRows(removedProxies);
        }
    } else {
        resetMessageCounts();
        fireMessageCountEvent(mce);
    }
}
