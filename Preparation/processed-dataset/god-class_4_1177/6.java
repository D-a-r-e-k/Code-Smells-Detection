/**
   * Push the record set forward value rows. Used to help in 
   * SQL pagination.
   * 
   * @param value
   */
public void skip(int value) {
    try {
        if (m_ResultSet != null)
            m_ResultSet.relative(value);
    } catch (Exception origEx) {
        // For now let's assume that the relative method is not supported.  
        // So let's do it manually.  
        try {
            for (int x = 0; x < value; x++) {
                if (!m_ResultSet.next())
                    break;
            }
        } catch (Exception e) {
            // If we still fail, add in both exceptions  
            m_XConnection.setError(origEx, this, checkWarnings());
            m_XConnection.setError(e, this, checkWarnings());
        }
    }
}
