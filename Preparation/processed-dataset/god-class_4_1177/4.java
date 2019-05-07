public void execute(XConnection xconn, SQLQueryParser query) throws SQLException {
    try {
        m_StreamingMode = "true".equals(xconn.getFeature("streaming"));
        m_MultipleResults = "true".equals(xconn.getFeature("multiple-results"));
        m_IsStatementCachingEnabled = "true".equals(xconn.getFeature("cache-statements"));
        m_XConnection = xconn;
        m_QueryParser = query;
        executeSQLStatement();
        createExpandedNameTable();
        // Start the document here  
        m_DocumentIdx = addElement(0, m_Document_TypeID, DTM.NULL, DTM.NULL);
        m_SQLIdx = addElement(1, m_SQL_TypeID, m_DocumentIdx, DTM.NULL);
        if (!m_MultipleResults)
            extractSQLMetaData(m_ResultSet.getMetaData());
    } catch (SQLException e) {
        m_HasErrors = true;
        throw e;
    }
}
