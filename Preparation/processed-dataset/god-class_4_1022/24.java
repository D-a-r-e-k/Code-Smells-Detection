/**
   * Loads the MessageInfos and MesageProxies.  Returns a List of
   * newly created MessageProxies.
   */
protected List createInfosAndProxies() throws MessagingException {
    int fetchBatchSize = 50;
    try {
        fetchBatchSize = Integer.parseInt(Pooka.getProperty("Pooka.fetchBatchSize", "50"));
    } catch (NumberFormatException nfe) {
    }
    Vector messageProxies = new Vector();
    Message[] msgs = folder.getMessages();
    Message[] toFetch = msgs;
    // go ahead and fetch the first set of messages; the rest will be 
    // taken care of by the loaderThread. 
    if (msgs.length > fetchBatchSize) {
        toFetch = new Message[fetchBatchSize];
        System.arraycopy(msgs, msgs.length - fetchBatchSize, toFetch, 0, fetchBatchSize);
    }
    folder.fetch(toFetch, fetchProfile);
    int firstFetched = Math.max(msgs.length - fetchBatchSize, 0);
    MessageInfo mi;
    for (int i = 0; i < msgs.length; i++) {
        mi = new MessageInfo(msgs[i], this);
        if (i >= firstFetched)
            mi.setFetched(true);
        messageProxies.add(new MessageProxy(getColumnValues(), mi));
        messageToInfoTable.put(msgs[i], mi);
    }
    return messageProxies;
}
