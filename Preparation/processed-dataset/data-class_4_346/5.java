// Implement DeclHandler  
/**
     *   Report an element type declaration.
     *  
     *   <p>The content model will consist of the string "EMPTY", the
     *   string "ANY", or a parenthesised group, optionally followed
     *   by an occurrence indicator.  The model will be normalized so
     *   that all whitespace is removed,and will include the enclosing
     *   parentheses.</p>
     *  
     *   @param name The element type name.
     *   @param model The content model as a normalized string.
     *   @exception SAXException The application may raise an exception.
     */
public void elementDecl(String name, String model) throws SAXException {
    // Do not inline external DTD  
    if (m_inExternalDTD)
        return;
    try {
        final java.io.Writer writer = m_writer;
        DTDprolog();
        writer.write("<!ELEMENT ");
        writer.write(name);
        writer.write(' ');
        writer.write(model);
        writer.write('>');
        writer.write(m_lineSep, 0, m_lineSepLen);
    } catch (IOException e) {
        throw new SAXException(e);
    }
}
