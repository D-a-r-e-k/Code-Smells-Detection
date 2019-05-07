/**
   * Gets the row number of the first unread message.  Returns -1 if
   * there are no unread messages, or if the FolderTableModel is not
   * set or empty.
   */
public int getFirstUnreadMessage() {
    // one part brute, one part force, one part ignorance. 
    if (getLogger().isLoggable(Level.FINE))
        getLogger().log(Level.FINE, "getting first unread message");
    if (!tracksUnreadMessages())
        return -1;
    if (getFolderTableModel() == null)
        return -1;
    if (isConnected()) {
        return super.getFirstUnreadMessage();
    } else {
        try {
            int countUnread = 0;
            int i;
            int unreadCount = cache.getUnreadMessageCount();
            if (unreadCount > 0) {
                long[] uids = getCache().getMessageUids();
                for (i = uids.length - 1; (i >= 0 && countUnread < unreadCount); i--) {
                    MessageInfo mi = getMessageInfoByUid(uids[i]);
                    if (!mi.getFlags().contains(Flags.Flag.SEEN))
                        countUnread++;
                }
                if (getLogger().isLoggable(Level.FINE))
                    getLogger().log(Level.FINE, "Returning " + i);
                return i + 1;
            } else {
                if (getLogger().isLoggable(Level.FINE))
                    getLogger().log(Level.FINE, "Returning -1");
                return -1;
            }
        } catch (MessagingException me) {
            if (getLogger().isLoggable(Level.FINE))
                getLogger().log(Level.FINE, "Messaging Exception.  Returning -1");
            return -1;
        }
    }
}
