/**
   * Unloads all of the tableInfos of the MessageInfo objects.  This
   * should be used either when the message information is stale, or when
   * the display rules have changed.
   */
public void unloadTableInfos() {
    if (folderTableModel != null) {
        List allProxies = folderTableModel.getAllProxies();
        for (int i = 0; i < allProxies.size(); i++) {
            MessageProxy mp = (MessageProxy) allProxies.get(i);
            mp.unloadTableInfo();
        }
        /*
        if (loaderThread != null)
        loaderThread.loadMessages(allProxies);
      */
        if (mMessageLoader != null)
            mMessageLoader.loadMessages(allProxies);
    }
}
