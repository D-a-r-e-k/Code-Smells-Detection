public SQLWarning checkWarnings() {
    SQLWarning warn = null;
    if (m_Statement != null) {
        try {
            warn = m_Statement.getWarnings();
            m_Statement.clearWarnings();
        } catch (SQLException se) {
        }
    }
    return (warn);
}
