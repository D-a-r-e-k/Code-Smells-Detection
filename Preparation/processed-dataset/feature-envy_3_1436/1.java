/**
   * Loads the messages in the queue.
   */
public void loadWaitingMessages() {
    int updateCounter = 0;
    int loadedMessageCount = 0;
    MessageProxy mp;
    // start this load section. 
    int queueSize = getQueueSize();
    int totalMessageCount = queueSize;
    if (!stopped && queueSize > 0) {
        folderInfo.getLogger().log(java.util.logging.Level.FINE, folderInfo.getFolderID() + " loading " + queueSize + " messages.");
        MessageLoadedListener display = getFolderInfo().getFolderDisplayUI();
        if (display != null)
            this.addMessageLoadedListener(display);
        fireMessageLoadedEvent(MessageLoadedEvent.LOADING_STARTING, 0, totalMessageCount);
        // get the batch sizes. 
        int fetchBatchSize = 50;
        int loadBatchSize = 25;
        try {
            fetchBatchSize = Integer.parseInt(Pooka.getProperty("Pooka.fetchBatchSize", "50"));
        } catch (NumberFormatException nfe) {
        }
        try {
            loadBatchSize = Integer.parseInt(Pooka.getProperty("Pooka.loadBatchSize", "25"));
        } catch (NumberFormatException nfe) {
        }
        FetchProfile fetchProfile = getFolderInfo().getFetchProfile();
        // we'll stay in this while loop until the queue is empty 
        for (List messages = retrieveNextBatch(fetchBatchSize); !stopped && messages != null; messages = retrieveNextBatch(fetchBatchSize)) {
            totalMessageCount = messages.size() + getQueueSize() + loadedMessageCount;
            if (Pooka.getProperty("Pooka.openFoldersInBackGround", "false").equalsIgnoreCase("true")) {
                synchronized (folderInfo.getFolderThread().getRunLock()) {
                    try {
                        folderInfo.getFolderThread().setCurrentActionName("Loading messages.");
                        // break when either we've been stopped or we're out of messages, 
                        for (int batchCount = 0; !stopped && batchCount < messages.size(); batchCount++) {
                            mp = (MessageProxy) messages.get(batchCount);
                            // if the message hasn't been fetched, then fetch fetchBatchSize 
                            // worth of messages. 
                            if (!mp.getMessageInfo().hasBeenFetched()) {
                                try {
                                    List fetchList = new ArrayList();
                                    for (int j = batchCount; fetchList.size() < fetchBatchSize && j < messages.size(); j++) {
                                        MessageInfo fetchInfo = ((MessageProxy) messages.get(j)).getMessageInfo();
                                        if (!fetchInfo.hasBeenFetched()) {
                                            fetchList.add(fetchInfo);
                                        }
                                    }
                                    MessageInfo[] toFetch = new MessageInfo[fetchList.size()];
                                    toFetch = (MessageInfo[]) fetchList.toArray(toFetch);
                                    getFolderInfo().fetch(toFetch, fetchProfile);
                                } catch (MessagingException me) {
                                    if (folderInfo.getLogger().isLoggable(java.util.logging.Level.WARNING)) {
                                        System.out.println("caught error while fetching for folder " + getFolderInfo().getFolderID() + ":  " + me);
                                        me.printStackTrace();
                                    }
                                }
                            }
                            // now load each individual messageproxy. 
                            // and refresh each message. 
                            try {
                                if (!mp.isLoaded())
                                    mp.loadTableInfo();
                                if (mp.needsRefresh())
                                    mp.refreshMessage();
                                else if (!mp.matchedFilters()) {
                                    mp.matchFilters();
                                }
                            } catch (Exception e) {
                                if (folderInfo.getLogger().isLoggable(java.util.logging.Level.WARNING)) {
                                    e.printStackTrace();
                                }
                            }
                            loadedMessageCount++;
                            if (++updateCounter >= getUpdateMessagesCount()) {
                                fireMessageLoadedEvent(MessageLoadedEvent.MESSAGES_LOADED, loadedMessageCount, totalMessageCount);
                                updateCounter = 0;
                            }
                        }
                    } finally {
                        folderInfo.getFolderThread().setCurrentActionName("");
                    }
                }
            } else {
                // break when either we've been stopped or we're out of messages, 
                for (int batchCount = 0; !stopped && batchCount < messages.size(); batchCount++) {
                    mp = (MessageProxy) messages.get(batchCount);
                    // if the message hasn't been fetched, then fetch fetchBatchSize 
                    // worth of messages. 
                    if (!mp.getMessageInfo().hasBeenFetched()) {
                        try {
                            List fetchList = new ArrayList();
                            for (int j = batchCount; fetchList.size() < fetchBatchSize && j < messages.size(); j++) {
                                MessageInfo fetchInfo = ((MessageProxy) messages.get(j)).getMessageInfo();
                                if (!fetchInfo.hasBeenFetched()) {
                                    fetchList.add(fetchInfo);
                                }
                            }
                            MessageInfo[] toFetch = new MessageInfo[fetchList.size()];
                            toFetch = (MessageInfo[]) fetchList.toArray(toFetch);
                            synchronized (folderInfo.getFolderThread().getRunLock()) {
                                folderInfo.getFolderThread().setCurrentActionName("Loading messages.");
                                getFolderInfo().fetch(toFetch, fetchProfile);
                                folderInfo.getFolderThread().setCurrentActionName("");
                            }
                        } catch (MessagingException me) {
                            if (getFolderInfo().getLogger().isLoggable(java.util.logging.Level.WARNING)) {
                                System.out.println("caught error while fetching for folder " + getFolderInfo().getFolderID() + ":  " + me);
                                me.printStackTrace();
                            }
                        }
                    }
                    // now load each individual messageproxy. 
                    // and refresh each message. 
                    try {
                        synchronized (folderInfo.getFolderThread().getRunLock()) {
                            try {
                                folderInfo.getFolderThread().setCurrentActionName("Loading messages.");
                                if (!mp.isLoaded())
                                    mp.loadTableInfo();
                                if (mp.needsRefresh())
                                    mp.refreshMessage();
                                else if (!mp.matchedFilters()) {
                                    mp.matchFilters();
                                }
                            } finally {
                                folderInfo.getFolderThread().setCurrentActionName("");
                            }
                        }
                    } catch (Exception e) {
                        if (folderInfo.getLogger().isLoggable(java.util.logging.Level.WARNING)) {
                            e.printStackTrace();
                        }
                    }
                    loadedMessageCount++;
                    if (++updateCounter >= getUpdateMessagesCount()) {
                        fireMessageLoadedEvent(MessageLoadedEvent.MESSAGES_LOADED, loadedMessageCount, totalMessageCount);
                        updateCounter = 0;
                    }
                }
            }
        }
        if (updateCounter > 0)
            fireMessageLoadedEvent(MessageLoadedEvent.MESSAGES_LOADED, loadedMessageCount, totalMessageCount);
        fireMessageLoadedEvent(MessageLoadedEvent.LOADING_COMPLETE, loadedMessageCount, totalMessageCount);
        if (display != null)
            removeMessageLoadedListener(display);
    }
}
