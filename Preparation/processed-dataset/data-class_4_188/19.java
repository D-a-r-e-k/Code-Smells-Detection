protected void runMessagesAdded(MessageCountEvent mce) {
    if (folderTableModel != null) {
        Message[] addedMessages = mce.getMessages();
        int fetchBatchSize = 25;
        int loadBatchSize = 25;
        try {
            fetchBatchSize = Integer.parseInt(Pooka.getProperty("Pooka.fetchBatchSize", "50"));
        } catch (NumberFormatException nfe) {
        }
        MessageInfo mi;
        Vector addedProxies = new Vector();
        List addedInfos = new java.util.ArrayList();
        for (int i = 0; i < addedMessages.length; i++) {
            if (addedMessages[i] instanceof CachingMimeMessage) {
                long uid = ((CachingMimeMessage) addedMessages[i]).getUID();
                mi = getMessageInfoByUid(uid);
                if (mi != null) {
                    addedInfos.add(mi);
                    if (getLogger().isLoggable(Level.FINE))
                        getLogger().log(Level.FINE, getFolderID() + ":  this is a duplicate.  not making a new messageinfo for it.");
                } else {
                    mi = new MessageInfo(addedMessages[i], CachingFolderInfo.this);
                    addedInfos.add(mi);
                    addedProxies.add(new MessageProxy(getColumnValues(), mi));
                    messageToInfoTable.put(addedMessages[i], mi);
                    uidToInfoTable.put(new Long(((CachingMimeMessage) addedMessages[i]).getUID()), mi);
                }
            } else {
                // it's a 'real' message from the server. 
                long uid = -1;
                try {
                    uid = getUID(addedMessages[i]);
                } catch (MessagingException me) {
                }
                mi = getMessageInfoByUid(uid);
                if (mi != null) {
                    addedInfos.add(mi);
                    if (getLogger().isLoggable(Level.FINE))
                        getLogger().log(Level.FINE, getFolderID() + ":  this is a duplicate.  not making a new messageinfo for it.");
                    // but we still need to autocache it if we're autocaching. 
                    if (autoCache) {
                        mMessageLoader.cacheMessages(new MessageProxy[] { getMessageInfoByUid(uid).getMessageProxy() });
                    }
                } else {
                    CachingMimeMessage newMsg = new CachingMimeMessage(CachingFolderInfo.this, uid);
                    mi = new MessageInfo(newMsg, CachingFolderInfo.this);
                    addedInfos.add(mi);
                    addedProxies.add(new MessageProxy(getColumnValues(), mi));
                    messageToInfoTable.put(newMsg, mi);
                    uidToInfoTable.put(new Long(uid), mi);
                }
            }
        }
        try {
            List preloadMessages = addedInfos;
            if (addedInfos.size() > fetchBatchSize) {
                preloadMessages = addedInfos.subList(0, fetchBatchSize);
            }
            MessageInfo[] preloadArray = new MessageInfo[preloadMessages.size()];
            for (int i = 0; i < preloadMessages.size(); i++) {
                preloadArray[i] = (MessageInfo) preloadMessages.get(i);
            }
            fetch(preloadArray, fetchProfile);
        } catch (MessagingException me) {
            getLogger().warning("error prefetching messages:  " + me.toString());
        }
        /*
        for (int i = 0; i < preloadMessages.length; i++) {
        long uid = -1;
        try {
        uid = getUID(preloadMessages[i]);
        } catch (MessagingException me) {
        }
        try {
        // FIXME
        getCache().cacheMessage((MimeMessage)preloadMessages[i], uid, getUIDValidity(), SimpleFileCache.FLAGS_AND_HEADERS, false);
        } catch (Exception e) {

        }
        }
      */
        getCache().writeMsgFile();
        clearStatusMessage(getFolderDisplayUI());
        addedProxies.removeAll(applyFilters(addedProxies));
        if (addedProxies.size() > 0) {
            if (getFolderTableModel() != null)
                getFolderTableModel().addRows(addedProxies);
            setNewMessages(true);
            resetMessageCounts();
            // notify the message loaded thread. 
            MessageProxy[] addedArray = (MessageProxy[]) addedProxies.toArray(new MessageProxy[0]);
            //loaderThread.loadMessages(addedArray, net.suberic.pooka.thread.LoadMessageThread.HIGH); 
            mMessageLoader.loadMessages(addedArray, net.suberic.pooka.thread.MessageLoader.HIGH);
            if (autoCache) {
                mMessageLoader.cacheMessages(addedArray);
            }
            // change the Message objects in the MessageCountEvent to 
            // our UIDMimeMessages. 
            Message[] newMsgs = new Message[addedProxies.size()];
            for (int i = 0; i < addedProxies.size(); i++) {
                newMsgs[i] = ((MessageProxy) addedProxies.elementAt(i)).getMessageInfo().getMessage();
            }
            MessageCountEvent newMce = new MessageCountEvent(getFolder(), mce.getType(), mce.isRemoved(), newMsgs);
            fireMessageCountEvent(newMce);
        }
    }
}
