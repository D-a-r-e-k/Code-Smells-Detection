/**
   * This sets the given Flag for all the MessageInfos given.
   */
public void setFlags(MessageInfo[] msgs, Flags flag, boolean value) throws MessagingException {
    // no optimization here. 
    for (int i = 0; i < msgs.length; i++) {
        msgs[i].getRealMessage().setFlags(flag, value);
    }
}
