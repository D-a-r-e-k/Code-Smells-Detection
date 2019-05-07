/**
     * Report an attribute type declaration.
     *
     * <p>Only the effective (first) declaration for an attribute will
     * be reported.  The type will be one of the strings "CDATA",
     * "ID", "IDREF", "IDREFS", "NMTOKEN", "NMTOKENS", "ENTITY",
     * "ENTITIES", or "NOTATION", or a parenthesized token group with
     * the separator "|" and all whitespace removed.</p>
     *
     * @param eName The name of the associated element.
     * @param aName The name of the attribute.
     * @param type A string representing the attribute type.
     * @param valueDefault A string representing the attribute default
     *        ("#IMPLIED", "#REQUIRED", or "#FIXED") or null if
     *        none of these applies.
     * @param value A string representing the attribute's default value,
     *        or null if there is none.
     * @exception SAXException The application may raise an exception.
     */
public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
    // Do not inline external DTD  
    if (m_inExternalDTD)
        return;
    try {
        final java.io.Writer writer = m_writer;
        DTDprolog();
        writer.write("<!ATTLIST ");
        writer.write(eName);
        writer.write(' ');
        writer.write(aName);
        writer.write(' ');
        writer.write(type);
        if (valueDefault != null) {
            writer.write(' ');
            writer.write(valueDefault);
        }
        //writer.write(" ");  
        //writer.write(value);  
        writer.write('>');
        writer.write(m_lineSep, 0, m_lineSepLen);
    } catch (IOException e) {
        throw new SAXException(e);
    }
}
