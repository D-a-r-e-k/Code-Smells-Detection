/**
     * Checks if there is already a PreparedStatement for retrieving the user-data and
     * constructs it, if it doesn't exist. Afterwards the login will be checked and
     * the user-object will be constructed if the credentials are correct. Null will
     * be returned, if the credentials did not return a user-record.
     * @return User the user which is allowed to log in or null if no match was found
     * @throws Exception if technical error occures (connection problems, ...)
     */
public User loginUser(String username, String password, String cookie) throws Exception {
    try {
        checkThread();
        PreparedStatement ps = getSelect();
        ps.setString(1, username.toLowerCase().trim());
        ResultSet rs = ps.executeQuery();
        sCnt++;
        Server.log(Thread.currentThread(), this.toString() + "LOGIN user uname=" + username.toLowerCase() + "/pwd=" + password + "/cookie=" + cookie + "\r\n" + selStrg, Server.MSG_AUTH, Server.LVL_VERY_VERBOSE);
        dbp.cacheMetaData(rs);
        if (!rs.next()) {
            Server.log(Thread.currentThread(), this.toString() + "LOGIN no user mathing username and password " + username + "/" + password, Server.MSG_AUTH, Server.LVL_MINOR);
            rs.close();
            // return unregistered user (if they are allowed will be checked in auth-manager)  
            return new User(username, cookie);
        } else if (!rs.isLast()) {
            Server.log(Thread.currentThread(), this.toString() + "LOGIN multible records returned for user " + username, Server.MSG_AUTH, Server.LVL_MAJOR);
            rs.close();
            // return null to make clear, that there is a problem within the db-table  
            return null;
        }
        checkThread();
        String dbpwd = rs.getString(dbp.columns.length + 1);
        if (dbpwd == null || !dbpwd.equals(password))
            return null;
        User u = new User(username, cookie);
        u.isUnregistered = false;
        readColumns(u, rs);
        checkWarnings(ps, "loginUser (getData)");
        checkThread();
        // if a lastlogin-property exists, we have to update the data in the db  
        if (!dbp.readOnly) {
            doLoginUpdates(u, rs);
        }
        checkWarnings(ps, "loginUser (update Data)");
        rs.close();
        Server.log(Thread.currentThread(), this.toString() + "LOGIN returning " + u, Server.MSG_AUTH, Server.LVL_MAJOR);
        u.isUnregistered = false;
        return u;
    } catch (Exception e) {
        Server.debug(this, selStrg, e, Server.MSG_AUTH, Server.LVL_MAJOR);
        isValid = false;
        release();
        throw e;
    }
}
