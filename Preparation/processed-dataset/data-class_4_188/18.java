/**
   * This synchronizes the cache with the new information from the
   * Folder.
   */
public void synchronizeCache() throws MessagingException {
    if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "synchronizing cache.");
    try {
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing", "Re-synchronizing with folder..."));
        if (getFolderDisplayUI() != null) {
            getFolderDisplayUI().setBusy(true);
        }
        long cacheUidValidity = getCache().getUIDValidity();
        if (uidValidity != cacheUidValidity) {
            showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("error.UIDFolder.validityMismatch", "Error:  validity not correct.  reloading..."));
            getCache().invalidateCache();
            getCache().setUIDValidity(uidValidity);
            cacheUidValidity = uidValidity;
        }
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.CachingFolder.synchronizing.writingChanges", "Writing local changes to server..."));
        // first write all the changes that we made back to the server. 
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronzing.writingChanges", "Writing local changes to server"));
        getCache().writeChangesToServer(getFolder());
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.loading", "Loading messages from folder..."));
        // load the list of uid's. 
        FetchProfile fp = new FetchProfile();
        fp.add(UIDFolder.FetchProfileItem.UID);
        // adding FLAGS to make getFirstUnreadMessage() more efficient 
        fp.add(FetchProfile.Item.FLAGS);
        if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "getting messages.");
        Message[] messages = getFolder().getMessages();
        if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "fetching messages.");
        String messageCount = messages == null ? "null" : Integer.toString(messages.length);
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.fetchingMessages", "Fetching") + " " + messageCount + " " + Pooka.getProperty("message.UIDFolder.synchronizing.messages", "messages."));
        getFolder().fetch(messages, fp);
        if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "done fetching messages.  getting uid's");
        long[] uids = new long[messages.length];
        for (int i = 0; i < messages.length; i++) {
            uids[i] = getUID(messages[i]);
        }
        if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "synchronizing--uids.length = " + uids.length);
        showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing", "Comparing new messages to current list..."));
        long[] addedUids = cache.getAddedMessages(uids, uidValidity);
        if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "synchronizing--addedUids.length = " + addedUids.length);
        if (addedUids.length > 0) {
            Message[] addedMsgs = ((UIDFolder) getFolder()).getMessagesByUID(addedUids);
            MessageCountEvent mce = new MessageCountEvent(getFolder(), MessageCountEvent.ADDED, false, addedMsgs);
            showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.loadingMessages", "Loading") + " " + addedUids.length + " " + Pooka.getProperty("message.UIDFolder.synchronizing.messages", "messages."));
            messagesAdded(mce);
        }
        long[] removedUids = cache.getRemovedMessages(uids, uidValidity);
        if (getLogger().isLoggable(Level.FINE))
            getLogger().log(Level.FINE, "synchronizing--removedUids.length = " + removedUids.length);
        if (removedUids.length > 0) {
            Message[] removedMsgs = new Message[removedUids.length];
            for (int i = 0; i < removedUids.length; i++) {
                MessageInfo mi = getMessageInfoByUid(removedUids[i]);
                if (mi != null)
                    removedMsgs[i] = mi.getMessage();
                if (removedMsgs[i] == null) {
                    removedMsgs[i] = new CachingMimeMessage(this, removedUids[i]);
                }
            }
            MessageCountEvent mce = new MessageCountEvent(getFolder(), MessageCountEvent.REMOVED, false, removedMsgs);
            showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.removingMessages", "Removing") + " " + removedUids.length + " " + Pooka.getProperty("message.UIDFolder.synchronizing.messages", "messages."));
            messagesRemoved(mce);
        }
        updateFlags(uids, messages, cacheUidValidity);
    } finally {
        if (getFolderDisplayUI() != null) {
            getFolderDisplayUI().clearStatusMessage();
            getFolderDisplayUI().setBusy(false);
        } else
            Pooka.getUIFactory().clearStatus();
    }
}
