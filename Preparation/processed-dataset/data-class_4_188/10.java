/**
   * Loads the MessageInfos and MesageProxies.  Returns a List of
   * newly created MessageProxies.
   */
protected List createInfosAndProxies() throws MessagingException {
    List messageProxies = new Vector();
    if (getStatus() > CONNECTED) {
        uidValidity = cache.getUIDValidity();
    }
    if (isConnected()) {
        try {
            // load the list of uid's. 
            showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.CachingFolder.synchronizing.writingChanges", "Writing local changes to server..."));
            // first write all the changes that we made back to the server. 
            getCache().writeChangesToServer(getFolder());
            showStatusMessage(getFolderDisplayUI(), Pooka.getProperty("message.UIDFolder.synchronizing.loading", "Loading messages from folder..."));
            FetchProfile uidFetchProfile = new FetchProfile();
            uidFetchProfile.add(UIDFolder.FetchProfileItem.UID);
            getLogger().log(Level.FINE, "getting messages.");
            Message[] messages = getFolder().getMessages();
            getLogger().log(Level.FINE, "fetching messages.");
            getFolder().fetch(messages, uidFetchProfile);
            getLogger().log(Level.FINE, "done fetching messages.  getting uid's");
            long[] uids = new long[messages.length];
            for (int i = 0; i < messages.length; i++) {
                uids[i] = getUID(messages[i]);
            }
            MessageInfo mi;
            for (int i = 0; i < uids.length; i++) {
                Message m = new CachingMimeMessage(this, uids[i]);
                mi = new MessageInfo(m, this);
                messageProxies.add(new MessageProxy(getColumnValues(), mi));
                messageToInfoTable.put(m, mi);
                uidToInfoTable.put(new Long(uids[i]), mi);
            }
            return messageProxies;
        } catch (Exception e) {
            final Exception fe = e;
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (getFolderDisplayUI() != null) {
                        getFolderDisplayUI().showError(Pooka.getProperty("error.CachingFolder.synchronzing", "Error synchronizing with folder"), Pooka.getProperty("error.CachingFolder.synchronzing.title", "Error synchronizing with folder"), fe);
                    } else {
                        Pooka.getUIFactory().showError(Pooka.getProperty("error.CachingFolder.synchronzing", "Error synchronizing with folder"), Pooka.getProperty("error.CachingFolder.synchronzing.title", "Error synchronizing with folder"), fe);
                    }
                }
            });
        }
    }
    long[] uids = cache.getMessageUids();
    MessageInfo mi;
    for (int i = 0; i < uids.length; i++) {
        Message m = new CachingMimeMessage(this, uids[i]);
        mi = new MessageInfo(m, this);
        MessageProxy mp = new MessageProxy(getColumnValues(), mi);
        mp.setRefresh(true);
        messageProxies.add(mp);
        messageToInfoTable.put(m, mi);
        uidToInfoTable.put(new Long(uids[i]), mi);
    }
    return messageProxies;
}
