private void synchronizeFlags() throws Exception, IOException, CommandCancelledException, IMAPException {
    printStatusMessage(MailResourceLoader.getString("statusbar", "message", "sync_flags"));
    // Build the remote lists of messages that are UNSEEN, FLAGGED, DELETED, 
    // JUNK 
    SearchKey unseenKey = new SearchKey(SearchKey.UNSEEN);
    List remoteUnseenUids = Arrays.asList(getServer().search(unseenKey, this));
    SearchKey flaggedKey = new SearchKey(SearchKey.FLAGGED);
    List remoteFlaggedUids = Arrays.asList(getServer().search(flaggedKey, this));
    SearchKey deletedKey = new SearchKey(SearchKey.DELETED);
    List remoteDeletedUids = Arrays.asList(getServer().search(deletedKey, this));
    SearchKey recentKey = new SearchKey(SearchKey.RECENT);
    List remoteRecentUids = Arrays.asList(getServer().search(recentKey, this));
    SearchKey junkKey = new SearchKey(SearchKey.KEYWORD, "JUNK");
    List remoteJunkUids = Arrays.asList(getServer().search(junkKey, this));
    // update the local flags and ensure that the MailboxInfo is correct 
    Enumeration uids = headerList.keys();
    int unseen = 0;
    while (uids.hasMoreElements()) {
        Object uid = uids.nextElement();
        IColumbaHeader header = headerList.get(uid);
        Flags flag = header.getFlags();
        Flags oldFlag = (Flags) flag.clone();
        flag.setSeen(Collections.binarySearch(remoteUnseenUids, uid) < 0);
        if (!flag.getSeen()) {
            unseen++;
        }
        flag.setDeleted(Collections.binarySearch(remoteDeletedUids, uid) >= 0);
        flag.setFlagged(Collections.binarySearch(remoteFlaggedUids, uid) >= 0);
        flag.setRecent(Collections.binarySearch(remoteRecentUids, uid) >= 0);
        header.getAttributes().put("columba.spam", new Boolean(Collections.binarySearch(remoteJunkUids, uid) >= 0));
        fireMessageFlagChanged(uid, oldFlag, 0);
    }
    syncMailboxInfo(getServer().getStatus(this));
}
