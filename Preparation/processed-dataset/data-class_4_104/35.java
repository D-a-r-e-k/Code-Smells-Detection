/**
    * checks if this Object is associated to a ban
    * @param o the Object to check
    * @return boolean true if this Object is associated with a ban
    */
public boolean isBanned(Object o) {
    // null is not ban-able  
    if (o == null)
        return false;
    // get the ban-object if there is one  
    BanObject b;
    if (o instanceof String) {
        String s = ((String) o).toLowerCase();
        b = (BanObject) banList.get(s);
    } else if (o instanceof Connection) {
        Connection conn = (Connection) o;
        if (!conn.isBanable())
            return false;
        b = (BanObject) banList.get(conn.getBanKey());
    } else if (o instanceof InetAddress) {
        InetAddress ia = (InetAddress) o;
        b = (BanObject) banList.get(ia.getHostAddress());
    } else {
        b = (BanObject) banList.get(o);
    }
    // not-banned if there is no ban-object  
    if (b == null)
        return false;
    // check if the ban is still fresh  
    if (b.time < System.currentTimeMillis()) {
        banList.remove(o);
        return false;
    }
    return true;
}
