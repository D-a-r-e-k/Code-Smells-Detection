protected void runMessagesAdded(MessageCountEvent mce) {
    folderLog(Level.FINE, "running messagesAdded on FolderInfo.");
    if (folderTableModel != null) {
        Message[] addedMessages = mce.getMessages();
        MessageInfo mp;
        Vector addedProxies = new Vector();
        folderLog(Level.FINE, "running messagesAdded: creating " + addedMessages.length + " proxies/MessageInfos.");
        for (int i = 0; i < addedMessages.length; i++) {
            mp = new MessageInfo(addedMessages[i], FolderInfo.this);
            addedProxies.add(new MessageProxy(getColumnValues(), mp));
            messageToInfoTable.put(addedMessages[i], mp);
        }
        folderLog(Level.FINE, "filtering proxies.");
        addedProxies.removeAll(applyFilters(addedProxies));
        if (addedProxies.size() > 0) {
            folderLog(Level.FINE, "filters run; adding " + addedProxies.size() + " messages.");
            getFolderTableModel().addRows(addedProxies);
            setNewMessages(true);
            resetMessageCounts();
            // notify the message loaded thread. 
            MessageProxy[] addedArray = (MessageProxy[]) addedProxies.toArray(new MessageProxy[0]);
            mMessageLoader.loadMessages(addedArray, MessageLoader.HIGH);
            fireMessageCountEvent(mce);
        }
    }
}
