/**
   * Gets the UID for the given Message.
   */
public long getUID(Message m) throws MessagingException {
    if (m instanceof SimpleFileCache.LocalMimeMessage) {
        return ((SimpleFileCache.LocalMimeMessage) m).getUID();
    } else {
        return super.getUID(m);
    }
}
