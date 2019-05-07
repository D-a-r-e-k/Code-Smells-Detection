/**
   * This sets the given Flag for all the MessageInfos given.
   */
public void setFlags(MessageInfo[] msgs, Flags flag, boolean value) throws MessagingException {
    Message[] m = new Message[msgs.length];
    for (int i = 0; i < msgs.length; i++) {
        m[i] = msgs[i].getRealMessage();
    }
    getFolder().setFlags(m, flag, value);
}
