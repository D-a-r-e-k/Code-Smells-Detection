/**
   * This does the real work when messages are removed.
   *
   * This method should always be run on the FolderThread.
   */
protected void runMessagesRemoved(MessageCountEvent mce) {
    Message[] removedMessages = mce.getMessages();
    Message[] removedCachingMessages = new Message[removedMessages.length];
    if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "removedMessages was of size " + removedMessages.length);
    MessageInfo mi;
    Vector removedProxies = new Vector();
    for (int i = 0; i < removedMessages.length; i++) {
        if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "checking for existence of message.");
        if (removedMessages[i] != null && removedMessages[i] instanceof CachingMimeMessage) {
            removedCachingMessages[i] = removedMessages[i];
            long uid = ((CachingMimeMessage) removedMessages[i]).getUID();
            mi = getMessageInfoByUid(uid);
            if (mi != null) {
                if (getLogger().isLoggable(Level.FINE))
                    getLogger().log(Level.FINE, "message exists--removing");
                if (mi.getMessageProxy() != null) {
                    mi.getMessageProxy().close();
                    removedProxies.add(mi.getMessageProxy());
                }
                messageToInfoTable.remove(mi);
                uidToInfoTable.remove(new Long(((CachingMimeMessage) removedMessages[i]).getUID()));
            }
            getCache().invalidateCache(((CachingMimeMessage) removedMessages[i]).getUID(), SimpleFileCache.MESSAGE);
        } else {
            // not a CachingMimeMessage. 
            long uid = -1;
            try {
                uid = getUID(removedMessages[i]);
            } catch (MessagingException me) {
            }
            mi = getMessageInfoByUid(uid);
            if (mi != null) {
                removedCachingMessages[i] = mi.getMessage();
                if (mi.getMessageProxy() != null)
                    mi.getMessageProxy().close();
                if (getLogger().isLoggable(Level.FINE))
                    getLogger().log(Level.FINE, "message exists--removing");
                Message localMsg = mi.getMessage();
                removedProxies.add(mi.getMessageProxy());
                messageToInfoTable.remove(localMsg);
                uidToInfoTable.remove(new Long(uid));
            } else {
                removedCachingMessages[i] = removedMessages[i];
            }
            getCache().invalidateCache(uid, SimpleFileCache.MESSAGE);
        }
    }
    MessageCountEvent newMce = new MessageCountEvent(getFolder(), mce.getType(), mce.isRemoved(), removedCachingMessages);
    if (getFolderDisplayUI() != null) {
        if (removedProxies.size() > 0) {
            getFolderDisplayUI().removeRows(removedProxies);
        }
        resetMessageCounts();
        fireMessageCountEvent(newMce);
    } else {
        resetMessageCounts();
        fireMessageCountEvent(newMce);
        if (removedProxies.size() > 0)
            getFolderTableModel().removeRows(removedProxies);
    }
}
