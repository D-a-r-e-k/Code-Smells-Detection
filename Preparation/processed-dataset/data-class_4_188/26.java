/**
   * This attempts to cache the message represented by this MessageInfo.
   */
public void cacheMessage(MessageInfo info, int cacheStatus) throws MessagingException {
    if (status == CONNECTED) {
        Message m = info.getMessage();
        if (m instanceof CachingMimeMessage) {
            long uid = ((CachingMimeMessage) m).getUID();
            MimeMessage realMessage = getRealMessageById(uid);
            getCache().cacheMessage(realMessage, uid, uidValidity, cacheStatus);
        } else if (m instanceof MimeMessage) {
            long uid = getUID(m);
            getCache().cacheMessage((MimeMessage) m, uid, uidValidity, cacheStatus);
        } else {
            throw new MessagingException(Pooka.getProperty("error.CachingFolderInfo.unknownMessageType", "Error:  unknownMessageType."));
        }
    } else {
        throw new MessagingException(Pooka.getProperty("error.CachingFolderInfo.cacheWhileDisconnected", "Error:  You cannot cache messages unless you\nare connected to the folder."));
    }
}
