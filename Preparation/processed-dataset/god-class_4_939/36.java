public BanObject getBanObject(Object o) {
    // null is not ban-able  
    if (o == null)
        return null;
    // get the ban-object if there is one  
    BanObject b;
    if (o instanceof String) {
        String s = ((String) o).toLowerCase();
        b = banList.get(s);
    } else if (o instanceof Connection) {
        Connection conn = (Connection) o;
        if (!conn.isBanable())
            return null;
        b = banList.get(conn.getBanKey());
    } else if (o instanceof InetAddress) {
        InetAddress ia = (InetAddress) o;
        b = banList.get(ia.getHostAddress());
    } else {
        b = banList.get(o);
    }
    return b;
}
