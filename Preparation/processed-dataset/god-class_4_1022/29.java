/**
   * Fetches the information for the given messages using the given
   * FetchProfile.
   */
public void fetch(MessageInfo[] messages, FetchProfile profile) throws MessagingException {
    Message[] realMsgs = new Message[messages.length];
    for (int i = 0; i < realMsgs.length; i++) {
        realMsgs[i] = messages[i].getMessage();
    }
    getFolder().fetch(realMsgs, profile);
    for (int i = 0; i < messages.length; i++) {
        messages[i].setFetched(true);
    }
}
