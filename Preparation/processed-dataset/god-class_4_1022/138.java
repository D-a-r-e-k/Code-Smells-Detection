/**
   * This forces an update of both the total and unread message counts.
   */
public void resetMessageCounts() {
    try {
        if (getFolder() != null)
            folderLog(Level.FINE, "running resetMessageCounts.  unread message count is " + getFolder().getUnreadMessageCount());
        else
            folderLog(Level.FINE, "running resetMessageCounts.  getFolder() is null.");
        if (tracksUnreadMessages()) {
            unreadCount = getFolder().getUnreadMessageCount();
        }
        messageCount = getFolder().getMessageCount();
    } catch (MessagingException me) {
        // if we lose the connection to the folder, we'll leave the old 
        // messageCount and set the unreadCount to zero. 
        unreadCount = 0;
    }
    updateNode();
}
