/**********************************************************************************************
 * BAN-METHODS (for checking and banning users...
 **********************************************************************************************/
/**
    * Used to ban a user from the server. Name and cookie are baned
    * If the ban-duration is reached, this ban will be removed by the main-thread
    * @param u the user object
    * @param msgTemplate the message template to use for this ban (eg. user.flood)
    * @param millis how long this user will be banned
    */
public void banUser(User u, String msgTemplate, String message, long millis, String bannedBy) {
    if (u == null)
        return;
    MessageParser mp = new MessageParser();
    mp.setSender(u);
    mp.setUsercontext(u);
    Group g = u.getGroup();
    if (g != null) {
        mp.setMessageTemplate(msgTemplate);
        g.removeUser(u);
        g.sendMessage(mp);
    }
    banUser(u, message, millis, bannedBy);
}
