public String getSQL() {
    StringBuffer sb = new StringBuffer();
    sb = new StringBuffer(64);
    sb.append(Tokens.T_CREATE).append(' ');
    if (isUnique()) {
        sb.append(Tokens.T_UNIQUE).append(' ');
    }
    sb.append(Tokens.T_INDEX).append(' ');
    sb.append(getName().statementName);
    sb.append(' ').append(Tokens.T_ON).append(' ');
    sb.append(((Table) table).getName().getSchemaQualifiedStatementName());
    int[] col = getColumns();
    int len = getVisibleColumns();
    sb.append(((Table) table).getColumnListSQL(col, len));
    return sb.toString();
}
