/**
   * Extract the Meta Data and build the Column Attribute List.
   * @param meta
   * @return
   */
private void extractSQLMetaData(ResultSetMetaData meta) {
    // Build the Node Tree, just add the Column Header  
    // branch now, the Row & col elements will be added  
    // on request.  
    // Add in the row-set Element  
    // Add in the MetaData Element  
    m_MetaDataIdx = addElement(1, m_MetaData_TypeID, m_MultipleResults ? m_RowSetIdx : m_SQLIdx, DTM.NULL);
    try {
        m_ColCount = meta.getColumnCount();
        m_ColHeadersIdx = new int[m_ColCount];
    } catch (Exception e) {
        m_XConnection.setError(e, this, checkWarnings());
    }
    // The ColHeaderIdx will be used to keep track of the  
    // Element entries for the individual Column Header.  
    int lastColHeaderIdx = DTM.NULL;
    // JDBC Columms Start at 1  
    int i = 1;
    for (i = 1; i <= m_ColCount; i++) {
        m_ColHeadersIdx[i - 1] = addElement(2, m_ColumnHeader_TypeID, m_MetaDataIdx, lastColHeaderIdx);
        lastColHeaderIdx = m_ColHeadersIdx[i - 1];
        // A bit brute force, but not sure how to clean it up  
        try {
            addAttributeToNode(meta.getColumnName(i), m_ColAttrib_COLUMN_NAME_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_COLUMN_NAME_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.getColumnLabel(i), m_ColAttrib_COLUMN_LABEL_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_COLUMN_LABEL_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.getCatalogName(i), m_ColAttrib_CATALOGUE_NAME_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_CATALOGUE_NAME_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(new Integer(meta.getColumnDisplaySize(i)), m_ColAttrib_DISPLAY_SIZE_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_DISPLAY_SIZE_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(new Integer(meta.getColumnType(i)), m_ColAttrib_COLUMN_TYPE_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_COLUMN_TYPE_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.getColumnTypeName(i), m_ColAttrib_COLUMN_TYPENAME_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_COLUMN_TYPENAME_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(new Integer(meta.getPrecision(i)), m_ColAttrib_PRECISION_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_PRECISION_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(new Integer(meta.getScale(i)), m_ColAttrib_SCALE_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_SCALE_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.getSchemaName(i), m_ColAttrib_SCHEMA_NAME_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_SCHEMA_NAME_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.getTableName(i), m_ColAttrib_TABLE_NAME_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_TABLE_NAME_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.isCaseSensitive(i) ? S_ISTRUE : S_ISFALSE, m_ColAttrib_CASESENSITIVE_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_CASESENSITIVE_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.isDefinitelyWritable(i) ? S_ISTRUE : S_ISFALSE, m_ColAttrib_DEFINITELYWRITEABLE_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_DEFINITELYWRITEABLE_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.isNullable(i) != 0 ? S_ISTRUE : S_ISFALSE, m_ColAttrib_ISNULLABLE_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_ISNULLABLE_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.isSigned(i) ? S_ISTRUE : S_ISFALSE, m_ColAttrib_ISSIGNED_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_ISSIGNED_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.isWritable(i) == true ? S_ISTRUE : S_ISFALSE, m_ColAttrib_ISWRITEABLE_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_ISWRITEABLE_TypeID, lastColHeaderIdx);
        }
        try {
            addAttributeToNode(meta.isSearchable(i) == true ? S_ISTRUE : S_ISFALSE, m_ColAttrib_ISSEARCHABLE_TypeID, lastColHeaderIdx);
        } catch (Exception e) {
            addAttributeToNode(S_ATTRIB_NOT_SUPPORTED, m_ColAttrib_ISSEARCHABLE_TypeID, lastColHeaderIdx);
        }
    }
}
