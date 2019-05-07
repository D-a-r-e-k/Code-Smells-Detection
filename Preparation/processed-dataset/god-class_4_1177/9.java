/**
   * Pull a record from the result set and map it to a DTM based ROW
   * If we are in Streaming mode, then only create a single row and
   * keep copying the data into the same row. This will keep the memory
   * footprint constint independant of the RecordSet Size. If we are not
   * in Streaming mode then create ROWS for the whole tree.
   * @return
   */
private boolean addRowToDTMFromResultSet() {
    try {
        // If we have not started the RowSet yet, then add it to the  
        // tree.  
        if (m_FirstRowIdx == DTM.NULL) {
            m_RowSetIdx = addElement(1, m_RowSet_TypeID, m_SQLIdx, m_MultipleResults ? m_RowSetIdx : m_MetaDataIdx);
            if (m_MultipleResults)
                extractSQLMetaData(m_ResultSet.getMetaData());
        }
        // Check to see if all the data has been read from the Query.  
        // If we are at the end the signal that event  
        if (!m_ResultSet.next()) {
            // In Streaming mode, the current ROW will always point back  
            // to itself until all the data was read. Once the Query is  
            // empty then point the next row to DTM.NULL so that the stream  
            // ends. Only do this if we have statted the loop to begin with.  
            if (m_StreamingMode && (m_LastRowIdx != DTM.NULL)) {
                // We are at the end, so let's untie the mark  
                m_nextsib.setElementAt(DTM.NULL, m_LastRowIdx);
            }
            m_ResultSet.close();
            if (m_MultipleResults) {
                while (!m_Statement.getMoreResults() && m_Statement.getUpdateCount() >= 0) ;
                m_ResultSet = m_Statement.getResultSet();
            } else
                m_ResultSet = null;
            if (m_ResultSet != null) {
                m_FirstRowIdx = DTM.NULL;
                addRowToDTMFromResultSet();
            } else {
                Vector parameters = m_QueryParser.getParameters();
                // Get output parameters.  
                if (parameters != null) {
                    int outParamIdx = addElement(1, m_OutParameter_TypeID, m_SQLIdx, m_RowSetIdx);
                    int lastColID = DTM.NULL;
                    for (int indx = 0; indx < parameters.size(); indx++) {
                        QueryParameter parm = (QueryParameter) parameters.elementAt(indx);
                        if (parm.isOutput()) {
                            Object rawobj = ((CallableStatement) m_Statement).getObject(indx + 1);
                            lastColID = addElementWithData(rawobj, 2, m_Col_TypeID, outParamIdx, lastColID);
                            addAttributeToNode(parm.getName(), m_ColAttrib_COLUMN_NAME_TypeID, lastColID);
                            addAttributeToNode(parm.getName(), m_ColAttrib_COLUMN_LABEL_TypeID, lastColID);
                            addAttributeToNode(new Integer(parm.getType()), m_ColAttrib_COLUMN_TYPE_TypeID, lastColID);
                            addAttributeToNode(parm.getTypeName(), m_ColAttrib_COLUMN_TYPENAME_TypeID, lastColID);
                        }
                    }
                }
                SQLWarning warn = checkWarnings();
                if (warn != null)
                    m_XConnection.setError(null, null, warn);
            }
            return false;
        }
        // If this is the first time here, start the new level  
        if (m_FirstRowIdx == DTM.NULL) {
            m_FirstRowIdx = addElement(2, m_Row_TypeID, m_RowSetIdx, m_MultipleResults ? m_MetaDataIdx : DTM.NULL);
            m_LastRowIdx = m_FirstRowIdx;
            if (m_StreamingMode) {
                // Let's tie the rows together until the end.  
                m_nextsib.setElementAt(m_LastRowIdx, m_LastRowIdx);
            }
        } else {
            //  
            // If we are in Streaming mode, then only use a single row instance  
            if (!m_StreamingMode) {
                m_LastRowIdx = addElement(2, m_Row_TypeID, m_RowSetIdx, m_LastRowIdx);
            }
        }
        // If we are not in streaming mode, this will always be DTM.NULL  
        // If we are in streaming mode, it will only be DTM.NULL the first time  
        int colID = _firstch(m_LastRowIdx);
        // Keep Track of who our parent was when adding new col objects.  
        int pcolID = DTM.NULL;
        // Columns in JDBC Start at 1 and go to the Extent  
        for (int i = 1; i <= m_ColCount; i++) {
            // Just grab the Column Object Type, we will convert it to a string  
            // later.  
            Object o = m_ResultSet.getObject(i);
            // Create a new column object if one does not exist.  
            // In Streaming mode, this mechinism will reuse the column  
            // data the second and subsequent row accesses.  
            if (colID == DTM.NULL) {
                pcolID = addElementWithData(o, 3, m_Col_TypeID, m_LastRowIdx, pcolID);
                cloneAttributeFromNode(pcolID, m_ColHeadersIdx[i - 1]);
            } else {
                // We must be in streaming mode, so let's just replace the data  
                // If the firstch was not set then we have a major error  
                int dataIdent = _firstch(colID);
                if (dataIdent == DTM.NULL) {
                    error("Streaming Mode, Data Error");
                } else {
                    m_ObjectArray.setAt(dataIdent, o);
                }
            }
            // If  
            // In streaming mode, this will be !DTM.NULL  
            // So if the elements were already established then we  
            // should be able to walk them in order.  
            if (colID != DTM.NULL) {
                colID = _nextsib(colID);
            }
        }
    } catch (Exception e) {
        if (DEBUG) {
            System.out.println("SQL Error Fetching next row [" + e.getLocalizedMessage() + "]");
        }
        m_XConnection.setError(e, this, checkWarnings());
        m_HasErrors = true;
    }
    // Only do a single row...  
    return true;
}
