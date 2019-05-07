void method0() { 
boolean m_flushedStartDoc = false;
/**
   * This is null unless we own the stream.
   */
private java.io.FileOutputStream m_outputStream = null;
/** The content handler where result events will be sent. */
private ContentHandler m_resultContentHandler;
/** The lexical handler where result events will be sent. */
private LexicalHandler m_resultLexicalHandler;
/** The DTD handler where result events will be sent. */
private DTDHandler m_resultDTDHandler;
/** The Decl handler where result events will be sent. */
private DeclHandler m_resultDeclHandler;
/** The Serializer, which may or may not be null. */
private Serializer m_serializer;
/** The Result object. */
private Result m_result;
/**
   * The system ID, which is unused, but must be returned to fullfill the
   *  TransformerHandler interface.
   */
private String m_systemID;
/**
   * The parameters, which is unused, but must be returned to fullfill the
   *  Transformer interface.
   */
private Hashtable m_params;
/** The error listener for TrAX errors and warnings. */
private ErrorListener m_errorListener = new org.apache.xml.utils.DefaultErrorHandler(false);
/**
   * The URIResolver, which is unused, but must be returned to fullfill the
   *  TransformerHandler interface.
   */
URIResolver m_URIResolver;
/** The output properties. */
private OutputProperties m_outputFormat;
/** Flag to set if we've found the first element, so we can tell if we have 
   *  to check to see if we should create an HTML serializer.      */
boolean m_foundFirstElement;
/**
   * State of the secure processing feature.
   */
private boolean m_isSecureProcessing = false;
}
