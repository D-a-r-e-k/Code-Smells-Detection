/**
   * Refreshes the flags for the given MessageInfo.
   */
public void refreshFlags(MessageInfo mi) throws MessagingException {
    if (isConnected())
        cacheMessage(mi, SimpleFileCache.FLAGS);
}
