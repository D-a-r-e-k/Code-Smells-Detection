public User loginUser(User u, String password) throws Exception {
    try {
        checkThread();
        PreparedStatement ps = getSelect();
        ps.setString(1, u.getName().toLowerCase().trim());
        Server.log(Thread.currentThread(), this.toString() + "LOGIN user uname=" + u.getName().toLowerCase() + "\r\n" + selStrg, Server.MSG_AUTH, Server.LVL_VERY_VERBOSE);
        ResultSet rs = ps.executeQuery();
        sCnt++;
        dbp.cacheMetaData(rs);
        if (!rs.next()) {
            Server.log(Thread.currentThread(), this.toString() + "LOGIN no user mathing username " + u.getName(), Server.MSG_AUTH, Server.LVL_MINOR);
            rs.close();
            return u;
        } else if (!rs.isLast()) {
            Server.log(Thread.currentThread(), this.toString() + "LOGIN multible records returned for user " + u.getName(), Server.MSG_AUTH, Server.LVL_MAJOR);
            rs.close();
            return u;
        }
        // always check Pwd if the userobject is marked as unregistered  
        // if there is an existing user having the same name but a differen password,  
        // we must return login-failed (done by returning null instead of an user-object)  
        if (u.isUnregistered == true) {
            String dbpwd = rs.getString(dbp.columns.length + 1);
            if (dbpwd == null || !dbpwd.equals(password)) {
                return null;
            }
            u.isUnregistered = false;
        }
        checkThread();
        // read all the other properties  
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
        return u;
    } catch (Exception e) {
        Server.log(this, selStrg, Server.MSG_AUTH, Server.LVL_MAJOR);
        isValid = false;
        release();
        throw e;
    }
}
