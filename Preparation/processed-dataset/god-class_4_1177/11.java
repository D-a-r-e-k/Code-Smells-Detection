/**
   * Close down any resources used by this document. If an SQL Error occure
   * while the document was being accessed, the SQL Connection used to create
   * this document will be released to the Connection Pool on error. This allows
   * the COnnection Pool to give special attention to any connection that may
   * be in a errored state.
   *
   */
public void close(boolean flushConnPool) {
    try {
        SQLWarning warn = checkWarnings();
        if (warn != null)
            m_XConnection.setError(null, null, warn);
    } catch (Exception e) {
    }
    try {
        if (null != m_ResultSet) {
            m_ResultSet.close();
            m_ResultSet = null;
        }
    } catch (Exception e) {
    }
    Connection conn = null;
    try {
        if (null != m_Statement) {
            conn = m_Statement.getConnection();
            m_Statement.close();
            m_Statement = null;
        }
    } catch (Exception e) {
    }
    try {
        if (conn != null) {
            if (m_HasErrors)
                m_ConnectionPool.releaseConnectionOnError(conn);
            else
                m_ConnectionPool.releaseConnection(conn);
        }
    } catch (Exception e) {
    }
    getManager().release(this, true);
}
