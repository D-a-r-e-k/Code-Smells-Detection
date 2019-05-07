// end new version 
/**
   * Refreshes the headers for the given MessageInfo.
   */
public void refreshHeaders(MessageInfo mi) throws MessagingException {
    cacheMessage(mi, SimpleFileCache.HEADERS);
}
