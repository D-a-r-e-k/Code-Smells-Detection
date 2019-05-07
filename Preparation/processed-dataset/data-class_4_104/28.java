/**
     * ban all users contained in given vector
     * @param v vector containing users
     * @param message reason for the kick
     * @param millis milliseconds this user will be banned
     * @param bannedBy name of the banner (may be Server in case of floodprotection
     */
public void banUser(Vector<User> v, String message, long millis, String bannedBy) {
    for (Enumeration<User> e = v.elements(); e.hasMoreElements(); ) {
        User u = (User) e.nextElement();
        this.banUser(u, message, millis, bannedBy);
    }
}
