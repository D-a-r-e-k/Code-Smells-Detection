/**
   * Loads all Messages into a new FolderTableModel, sets this
   * FolderTableModel as the current FolderTableModel, and then returns
   * said FolderTableModel.  This is the basic way to populate a new
   * FolderTableModel.
   */
public synchronized void loadAllMessages() throws MessagingException, OperationCancelledException {
    if (folderTableModel == null) {
        updateDisplay(true);
        if (!isLoaded())
            loadFolder();
        fetchProfile = createColumnInformation();
        /*
        if (loaderThread == null)
        loaderThread = createLoaderThread();
      */
        if (mMessageLoader == null)
            mMessageLoader = createMessageLoader();
        try {
            updateFolderStatusForLoading();
            List messageProxies = createInfosAndProxies();
            runFilters(messageProxies);
            FolderTableModel ftm = new FolderTableModel(messageProxies, getColumnNames(), getColumnSizes(), getColumnValues(), getColumnIds());
            setFolderTableModel(ftm);
            updateCache();
            Vector loadImmediately = null;
            int loadBatchSize = 25;
            if (messageProxies.size() > loadBatchSize) {
                // get the first unread. 
                int firstUnread = messageProxies.size();
                if (Pooka.getProperty("Pooka.autoSelectFirstUnread", "true").equalsIgnoreCase("true")) {
                    firstUnread = getFirstUnreadMessage();
                }
                int lastLoaded = messageProxies.size() - 1;
                int firstLoaded = messageProxies.size() - loadBatchSize - 1;
                if (firstUnread > -1) {
                    if (firstUnread < firstLoaded) {
                        firstLoaded = Math.max(0, firstUnread - 5);
                        lastLoaded = firstLoaded + loadBatchSize;
                    }
                }
                loadImmediately = new Vector();
                for (int i = lastLoaded; i >= firstLoaded; i--) {
                    loadImmediately.add(messageProxies.get(i));
                }
            } else {
                loadImmediately = new Vector(messageProxies);
            }
            loadMessageTableInfos(loadImmediately);
            /*
          loaderThread.loadMessages(messageProxies);

          if (!loaderThread.isAlive())
          loaderThread.start();
        */
            mMessageLoader.loadMessages(messageProxies);
        } finally {
            updateDisplay(false);
        }
    }
}
