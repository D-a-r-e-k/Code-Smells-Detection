void method0() { 
// 
// Constants 
// 
// features 
/** Document fragment balancing only. */
protected static final String DOCUMENT_FRAGMENT = "http://cyberneko.org/html/features/document-fragment";
/** Recognized features. */
protected static final String[] RECOGNIZED_FEATURES = { DOCUMENT_FRAGMENT };
// properties 
/** Property identifier: error handler. */
protected static final String ERROR_HANDLER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_HANDLER_PROPERTY;
/** Current element node. */
protected static final String CURRENT_ELEMENT_NODE = Constants.XERCES_PROPERTY_PREFIX + Constants.CURRENT_ELEMENT_NODE_PROPERTY;
/** Recognized properties. */
protected static final String[] RECOGNIZED_PROPERTIES = { ERROR_HANDLER, CURRENT_ELEMENT_NODE };
// 
// Data 
// 
/** Parser configuration. */
protected XMLParserConfiguration fParserConfiguration;
/** Document source. */
protected XMLDocumentSource fDocumentSource;
/** DOM document fragment. */
protected DocumentFragment fDocumentFragment;
/** Document. */
protected Document fDocument;
/** Current node. */
protected Node fCurrentNode;
/** True if within a CDATA section. */
protected boolean fInCDATASection;
}
