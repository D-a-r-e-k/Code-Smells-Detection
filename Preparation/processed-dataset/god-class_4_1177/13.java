/**
   * @param identity
   * @return
   */
protected int _nextsib(int identity) {
    // If we are asking for the next row and we have not  
    // been there yet then let's see if we can get another  
    // row from the ResultSet.  
    //  
    if (m_ResultSet != null) {
        int id = _exptype(identity);
        // We need to prime the pump since we don't do it in execute any more.  
        if (m_FirstRowIdx == DTM.NULL) {
            addRowToDTMFromResultSet();
        }
        if ((id == m_Row_TypeID) && (identity >= m_LastRowIdx)) {
            if (DEBUG)
                System.out.println("reading from the ResultSet");
            addRowToDTMFromResultSet();
        } else if (m_MultipleResults && identity == m_RowSetIdx) {
            if (DEBUG)
                System.out.println("reading for next ResultSet");
            int startIdx = m_RowSetIdx;
            while (startIdx == m_RowSetIdx && m_ResultSet != null) addRowToDTMFromResultSet();
        }
    }
    return super._nextsib(identity);
}
