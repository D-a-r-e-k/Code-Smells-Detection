/**
     * Checks if a PreparedStatement for selection is already constructed or a 
     * new PreparedStatement will be constructed and this PreparedStatement will
     * be returned
     * @return the PreparedStatement for selection
     * @throws Exception if an Error occured
     */
private PreparedStatement getSelect() throws Exception {
    try {
        if (select != null)
            return select;
        if (con == null)
            throw new Exception("No connection to retrieve a PreparedStatement from");
        StringBuffer sb = new StringBuffer("SELECT ");
        sb.append(dbp.columns[0]);
        for (int i = 1; i < dbp.columns.length; i++) {
            sb.append(", ");
            sb.append(dbp.columns[i]);
        }
        sb.append(", ");
        sb.append(dbp.fc_password);
        sb.append(" FROM ");
        sb.append(dbp.table);
        sb.append(" WHERE ");
        sb.append(dbp.fc_username);
        sb.append(" = ?");
        selStrg = sb.toString();
        select = con.prepareStatement(selStrg, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        if (!dbp.RS_TYPE_SCROLL_SENSITIV) {
            select = con.prepareStatement(selStrg, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } else
            select = con.prepareStatement(selStrg, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        if (dbp.queryTimeout > 0)
            select.setQueryTimeout(dbp.queryTimeout);
        return select;
    } catch (Exception e) {
        isValid = false;
        release();
        throw e;
    }
}
