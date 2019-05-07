/*
    public int getUnreadCount() {
    if (! tracksUnreadMessages())
    return -1;
    else {
    try {
    if (getCache() != null)
    unreadCount = getCache().getUnreadMessageCount();
    } catch (MessagingException me) {

    }
    return unreadCount;
    }
    }

    public int getMessageCount() {
    try {
    if (getCache() != null)
    messageCount = getCache().getMessageCount();
    } catch (MessagingException me) {
    }
    return messageCount;
    }
  */
/**
   * This forces an update of both the total and unread message counts.
   */
public void resetMessageCounts() {
    try {
        if (getLogger().isLoggable(Level.FINE)) {
            if (getFolder() != null)
                getLogger().log(Level.FINE, "running resetMessageCounts.  unread message count is " + getFolder().getUnreadMessageCount());
            else
                getLogger().log(Level.FINE, "running resetMessageCounts.  getFolder() is null.");
        }
        if (isConnected()) {
            if (tracksUnreadMessages())
                unreadCount = getFolder().getUnreadMessageCount();
            messageCount = getFolder().getMessageCount();
        } else if (getCache() != null) {
            messageCount = getCache().getMessageCount();
            if (tracksUnreadMessages())
                unreadCount = getCache().getUnreadMessageCount();
        } else {
        }
    } catch (MessagingException me) {
        // if we lose the connection to the folder, we'll leave the old 
        // messageCount and set the unreadCount to zero. 
        unreadCount = 0;
    }
    updateNode();
}
