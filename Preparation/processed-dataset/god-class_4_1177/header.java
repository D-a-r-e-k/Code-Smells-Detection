void method0() { 
/**
   */
private boolean DEBUG = false;
/**
   */
private static final String S_NAMESPACE = "http://xml.apache.org/xalan/SQLExtension";
/**
   */
private static final String S_SQL = "sql";
/**
   */
private static final String S_ROW_SET = "row-set";
/**
   */
private static final String S_METADATA = "metadata";
/**
   */
private static final String S_COLUMN_HEADER = "column-header";
/**
   */
private static final String S_ROW = "row";
/**
   */
private static final String S_COL = "col";
/**
   */
private static final String S_OUT_PARAMETERS = "out-parameters";
/**
   */
private static final String S_CATALOGUE_NAME = "catalogue-name";
/**
   */
private static final String S_DISPLAY_SIZE = "column-display-size";
/**
   */
private static final String S_COLUMN_LABEL = "column-label";
/**
   */
private static final String S_COLUMN_NAME = "column-name";
/**
   */
private static final String S_COLUMN_TYPE = "column-type";
/**
   */
private static final String S_COLUMN_TYPENAME = "column-typename";
/**
   */
private static final String S_PRECISION = "precision";
/**
   */
private static final String S_SCALE = "scale";
/**
   */
private static final String S_SCHEMA_NAME = "schema-name";
/**
   */
private static final String S_TABLE_NAME = "table-name";
/**
   */
private static final String S_CASESENSITIVE = "case-sensitive";
/**
   */
private static final String S_DEFINITELYWRITABLE = "definitely-writable";
/**
   */
private static final String S_ISNULLABLE = "nullable";
/**
   */
private static final String S_ISSIGNED = "signed";
/**
   */
private static final String S_ISWRITEABLE = "writable";
/**
   */
private static final String S_ISSEARCHABLE = "searchable";
/**
   */
private int m_SQL_TypeID = 0;
/**
   */
private int m_MetaData_TypeID = 0;
/**
   */
private int m_ColumnHeader_TypeID = 0;
/**
   */
private int m_RowSet_TypeID = 0;
/**
   */
private int m_Row_TypeID = 0;
/**
   */
private int m_Col_TypeID = 0;
/**
   */
private int m_OutParameter_TypeID = 0;
/**
   */
private int m_ColAttrib_CATALOGUE_NAME_TypeID = 0;
/**
   */
private int m_ColAttrib_DISPLAY_SIZE_TypeID = 0;
/**
   */
private int m_ColAttrib_COLUMN_LABEL_TypeID = 0;
/**
   */
private int m_ColAttrib_COLUMN_NAME_TypeID = 0;
/**
   */
private int m_ColAttrib_COLUMN_TYPE_TypeID = 0;
/**
   */
private int m_ColAttrib_COLUMN_TYPENAME_TypeID = 0;
/**
   */
private int m_ColAttrib_PRECISION_TypeID = 0;
/**
   */
private int m_ColAttrib_SCALE_TypeID = 0;
/**
   */
private int m_ColAttrib_SCHEMA_NAME_TypeID = 0;
/**
   */
private int m_ColAttrib_TABLE_NAME_TypeID = 0;
/**
   */
private int m_ColAttrib_CASESENSITIVE_TypeID = 0;
/**
   */
private int m_ColAttrib_DEFINITELYWRITEABLE_TypeID = 0;
/**
   */
private int m_ColAttrib_ISNULLABLE_TypeID = 0;
/**
   */
private int m_ColAttrib_ISSIGNED_TypeID = 0;
/**
   */
private int m_ColAttrib_ISWRITEABLE_TypeID = 0;
/**
   */
private int m_ColAttrib_ISSEARCHABLE_TypeID = 0;
/**
   * The Statement used to extract the data from the database connection.
   */
private Statement m_Statement = null;
/**
   * Expression COntext used to creat this document
   * may be used to grab variables from the XSL processor
   */
private ExpressionContext m_ExpressionContext = null;
/**
   * The Connection Pool where we has derived all of our connections
   * for this document
   */
private ConnectionPool m_ConnectionPool = null;
/**
   * The current ResultSet.
   */
private ResultSet m_ResultSet = null;
/**
   * The parameter definitions if this is a callable
   * statement with output parameters.
   */
private SQLQueryParser m_QueryParser = null;
/**
   * As the column header array is built, keep the node index
   * for each Column.
   * The primary use of this is to locate the first attribute for
   * each column in each row as we add records.
   */
private int[] m_ColHeadersIdx;
/**
   * An indicator on how many columns are in this query
   */
private int m_ColCount;
/**
   * The Index of the MetaData Node. Currently the MetaData Node contains the
   *
   */
private int m_MetaDataIdx = DTM.NULL;
/**
   * The index of the Row Set node. This is the sibling directly after
   * the last Column Header.
   */
private int m_RowSetIdx = DTM.NULL;
/**
   */
private int m_SQLIdx = DTM.NULL;
/**
   * Demark the first row element where we started adding rows into the
   * Document.
   */
private int m_FirstRowIdx = DTM.NULL;
/**
   * Keep track of the Last row inserted into the DTM from the ResultSet.
   * This will be used as the index of the parent Row Element when adding
   * a row.
   */
private int m_LastRowIdx = DTM.NULL;
/**
   * Streaming Mode Control, In Streaming mode we reduce the memory
   * footprint since we only use a single row instance.
   */
private boolean m_StreamingMode = true;
/**
   * Multiple Result sets mode (metadata inside rowset).
   */
private boolean m_MultipleResults = false;
/**
   * Flag to detect if an error occured during an operation
   * Defines how errors are handled and how the SQL Connection
   * is closed.
   */
private boolean m_HasErrors = false;
/**
   * Is statement caching enabled.
   */
private boolean m_IsStatementCachingEnabled = false;
/**
   * XConnection this document came from.
   */
private XConnection m_XConnection = null;
}
