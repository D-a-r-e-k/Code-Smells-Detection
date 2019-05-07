private PreparedStatement getUpdate() throws Exception {
    try {
        if (update != null)
            return update;
        if (con == null)
            throw new Exception("No connection to retrieve a PreparedStatement from");
        StringBuffer sb = new StringBuffer("UPDATE ");
        sb.append(dbp.table);
        sb.append(" SET ");
        sb.append(dbp.updCols[0]);
        sb.append(" = ?");
        for (int i = 1; i < dbp.updCols.length; i++) {
            sb.append(", ");
            sb.append(dbp.updCols[i]);
            sb.append(" = ?");
        }
        sb.append(" WHERE ");
        if (dbp.idField != null) {
            sb.append(dbp.idField);
            sb.append(" = ?");
        } else {
            sb.append(dbp.fc_username);
            sb.append(" = ?");
        }
        updStrg = sb.toString();
        update = con.prepareStatement(updStrg);
        if (dbp.queryTimeout > 0)
            update.setQueryTimeout(dbp.queryTimeout);
        return update;
    } catch (Exception e) {
        isValid = false;
        release();
        throw e;
    }
}
