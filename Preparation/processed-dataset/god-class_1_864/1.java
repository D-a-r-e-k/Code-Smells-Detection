// <init>() 
// 
// Public methods 
// 
/** Parses a document fragment. */
public void parse(String systemId, DocumentFragment fragment) throws SAXException, IOException {
    parse(new InputSource(systemId), fragment);
}
