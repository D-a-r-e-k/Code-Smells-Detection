private void executeSQLStatement() throws SQLException {
    m_ConnectionPool = m_XConnection.getConnectionPool();
    Connection conn = m_ConnectionPool.getConnection();
    if (!m_QueryParser.hasParameters()) {
        m_Statement = conn.createStatement();
        m_ResultSet = m_Statement.executeQuery(m_QueryParser.getSQLQuery());
    } else if (m_QueryParser.isCallable()) {
        CallableStatement cstmt = conn.prepareCall(m_QueryParser.getSQLQuery());
        m_QueryParser.registerOutputParameters(cstmt);
        m_QueryParser.populateStatement(cstmt, m_ExpressionContext);
        m_Statement = cstmt;
        if (!cstmt.execute())
            throw new SQLException("Error in Callable Statement");
        m_ResultSet = m_Statement.getResultSet();
    } else {
        PreparedStatement stmt = conn.prepareStatement(m_QueryParser.getSQLQuery());
        m_QueryParser.populateStatement(stmt, m_ExpressionContext);
        m_Statement = stmt;
        m_ResultSet = stmt.executeQuery();
    }
}
