/**
   * Gets the row number of the first unread message.  Returns -1 if
   * there are no unread messages, or if the FolderTableModel is not
   * set or empty.
   */
public int getFirstUnreadMessage() {
    folderLog(Level.FINE, "getting first unread message");
    if (!tracksUnreadMessages())
        return -1;
    if (getFolderTableModel() == null)
        return -1;
    try {
        int countUnread = 0;
        int i;
        if (unreadCount > 0) {
            // one part brute, one part force, one part ignorance. 
            Message[] messages = getFolder().getMessages();
            int lastUnreadFound = -1;
            for (i = messages.length - 1; (i >= 0 && countUnread < unreadCount); i--) {
                if (!(messages[i].isSet(Flags.Flag.SEEN))) {
                    lastUnreadFound = i;
                    countUnread++;
                }
            }
            if (lastUnreadFound != -1) {
                folderLog(Level.FINE, "Returning " + (lastUnreadFound + 1));
                return lastUnreadFound;
            } else {
                folderLog(Level.FINE, "unreads detected, but none found.");
                return -1;
            }
        } else {
            folderLog(Level.FINE, "Returning -1");
            return -1;
        }
    } catch (MessagingException me) {
        folderLog(Level.FINE, "Messaging Exception.  Returning -1");
        return -1;
    }
}
