/**
   * gets all of the message proxies associated with this folder info
   * and notifies them that they need to rematch their filters.
   */
protected void rematchFilters() {
    if (folderTableModel != null) {
        List allProxies = folderTableModel.getAllProxies();
        for (int i = 0; i < allProxies.size(); i++) {
            ((MessageProxy) allProxies.get(i)).clearMatchedFilters();
        }
        //loaderThread.loadMessages(allProxies); 
        mMessageLoader.loadMessages(allProxies);
    }
}
