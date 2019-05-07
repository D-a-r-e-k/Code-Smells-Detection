protected void runMessageChanged(MessageChangedEvent mce) {
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
            MessageInfo mi = getMessageInfo(mce.getMessage());
            MessageProxy mp = mi.getMessageProxy();
            if (mp != null) {
                mp.unloadTableInfo();
                mp.loadTableInfo();
                if (mce.getMessageChangeType() == MessageChangedEvent.FLAGS_CHANGED)
                    mi.refreshFlags();
                else if (mce.getMessageChangeType() == MessageChangedEvent.ENVELOPE_CHANGED)
                    mi.refreshHeaders();
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
