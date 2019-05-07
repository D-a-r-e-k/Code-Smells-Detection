/**
   * Returns whether or not we should be showing cache information in
   * the FolderDisplay.  Uses the FolderProperty.showCacheInfo property
   * to determine--if this is set to true, we will show the cache info.
   * Otherwise, if we're connected, don't show the info, and if we're
   * not connected, do.
   */
public boolean showCacheInfo() {
    if (Pooka.getProperty(getFolderProperty() + ".showCacheInfo", "false").equalsIgnoreCase("true"))
        return true;
    else {
        if (getStatus() == CONNECTED) {
            return false;
        } else
            return true;
    }
}
