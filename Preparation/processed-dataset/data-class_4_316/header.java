void method0() { 
// TODO: When DOM Level 3 goes to REC replace method calls using  
// reflection for: getXmlEncoding, getInputEncoding and getXmlEncoding  
// with regular static calls on the Document object.  
// data  
// serializer  
private XMLSerializer serializer;
// XML 1.1 serializer  
private XML11Serializer xml11Serializer;
//Recognized parameters  
private DOMStringList fRecognizedParameters;
/** REVISIT: Currently we handle 3 different configurations, would be nice just have one configuration
     * that has different recognized parameters depending if it is used in Core/LS. 
     */
protected short features = 0;
protected static final short NAMESPACES = 0x1 << 0;
protected static final short WELLFORMED = 0x1 << 1;
protected static final short ENTITIES = 0x1 << 2;
protected static final short CDATA = 0x1 << 3;
protected static final short SPLITCDATA = 0x1 << 4;
protected static final short COMMENTS = 0x1 << 5;
protected static final short DISCARDDEFAULT = 0x1 << 6;
protected static final short INFOSET = 0x1 << 7;
protected static final short XMLDECL = 0x1 << 8;
protected static final short NSDECL = 0x1 << 9;
protected static final short DOM_ELEMENT_CONTENT_WHITESPACE = 0x1 << 10;
protected static final short PRETTY_PRINT = 0x1 << 11;
// well-formness checking  
private DOMErrorHandler fErrorHandler = null;
private final DOMErrorImpl fError = new DOMErrorImpl();
private final DOMLocatorImpl fLocator = new DOMLocatorImpl();
}
