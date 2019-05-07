/**
   * This updates the TableInfo on the changed messages.
   *
   * As defined by java.mail.MessageChangedListener.
   */
public void runMessageChanged(MessageChangedEvent mce) {
    // if the message is getting deleted, then we don't 
    // really need to update the table info.  for that 
    // matter, it's likely that we'll get MessagingExceptions 
    // if we do, anyway. 
    boolean updateInfo = false;
    try {
        updateInfo = (!mce.getMessage().isSet(Flags.Flag.DELETED) || !Pooka.getProperty("Pooka.autoExpunge", "true").equalsIgnoreCase("true"));
    } catch (MessagingException me) {
    }
    if (updateInfo) {
        try {
            Message msg = mce.getMessage();
            long uid = -1;
            uid = getUID(msg);
            if (msg != null) {
                if (mce.getMessageChangeType() == MessageChangedEvent.FLAGS_CHANGED)
                    getCache().cacheMessage((MimeMessage) msg, uid, uidValidity, SimpleFileCache.FLAGS);
                else if (mce.getMessageChangeType() == MessageChangedEvent.ENVELOPE_CHANGED)
                    getCache().cacheMessage((MimeMessage) msg, uid, uidValidity, SimpleFileCache.HEADERS);
            }
            MessageInfo mi = getMessageInfoByUid(uid);
            if (mi != null) {
                MessageProxy mp = mi.getMessageProxy();
                if (mp != null) {
                    mp.unloadTableInfo();
                    mp.loadTableInfo();
                }
            }
        } catch (MessagingException me) {
        }
        // if we're not just a tableinfochanged event, do a resetmessagecouts. 
        // don't do this if we're just a delete. 
        if (!(mce instanceof net.suberic.pooka.event.MessageTableInfoChangedEvent)) {
            resetMessageCounts();
        }
    }
    fireMessageChangedEvent(mce);
}
