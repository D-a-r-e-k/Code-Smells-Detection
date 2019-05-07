/**
   * Loads the FolderTableInfo objects for the given messages.
   */
public void loadMessageTableInfos(Vector messages) {
    int numMessages = messages.size();
    MessageProxy mp;
    int updateCounter = 0;
    if (numMessages > 0) {
        int fetchBatchSize = 25;
        int loadBatchSize = 25;
        try {
            fetchBatchSize = Integer.parseInt(Pooka.getProperty("Pooka.fetchBatchSize", "50"));
        } catch (NumberFormatException nfe) {
        }
        FetchProfile fetchProfile = getFetchProfile();
        int i = numMessages - 1;
        while (i >= 0) {
            for (int batchCount = 0; i >= 0 && batchCount < loadBatchSize; batchCount++) {
                mp = (MessageProxy) messages.elementAt(i);
                if (!mp.getMessageInfo().hasBeenFetched()) {
                    try {
                        int fetchCount = 0;
                        Vector fetchVector = new Vector();
                        for (int j = i; fetchCount < fetchBatchSize && j >= 0; j--) {
                            MessageInfo fetchInfo = ((MessageProxy) messages.elementAt(j)).getMessageInfo();
                            if (!fetchInfo.hasBeenFetched()) {
                                fetchVector.add(fetchInfo);
                                fetchInfo.setFetched(true);
                            }
                        }
                        MessageInfo[] toFetch = new MessageInfo[fetchVector.size()];
                        toFetch = (MessageInfo[]) fetchVector.toArray(toFetch);
                        this.fetch(toFetch, fetchProfile);
                    } catch (MessagingException me) {
                        folderLog(Level.FINE, "caught error while fetching for folder " + getFolderID() + ":  " + me);
                        me.printStackTrace();
                    }
                }
                try {
                    if (!mp.isLoaded())
                        mp.loadTableInfo();
                    if (mp.needsRefresh())
                        mp.refreshMessage();
                    else if (!mp.matchedFilters()) {
                        mp.matchFilters();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i--;
            }
        }
    }
}
