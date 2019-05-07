/**
   * Unloads the matching filters.
   */
public void unloadMatchingFilters() {
    if (folderTableModel != null) {
        List allProxies = folderTableModel.getAllProxies();
        for (int i = 0; i < allProxies.size(); i++) {
            MessageProxy mp = (MessageProxy) allProxies.get(i);
            mp.clearMatchedFilters();
        }
        /*
        if (loaderThread != null)
        loaderThread.loadMessages(allProxies);
      */
        if (mMessageLoader != null)
            mMessageLoader.loadMessages(allProxies);
    }
}
