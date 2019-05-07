// setProperty(String,Object) 
/**
     * Query the value of a property.
     *
     * Return the current value of a property in a SAX2 parser.
     * The parser might not recognize the property.
     *
     * @param propertyId The unique identifier (URI) of the property
     *                   being set.
     * @return The current value of the property.
     * @exception org.xml.sax.SAXNotRecognizedException If the
     *            requested property is not known.
     * @exception SAXNotSupportedException If the
     *            requested property is known but not supported.
     */
public Object getProperty(String propertyId) throws SAXNotRecognizedException, SAXNotSupportedException {
    if (propertyId.equals(CURRENT_ELEMENT_NODE)) {
        return (fCurrentNode != null && fCurrentNode.getNodeType() == Node.ELEMENT_NODE) ? fCurrentNode : null;
    }
    try {
        return fParserConfiguration.getProperty(propertyId);
    } catch (XMLConfigurationException e) {
        String message = e.getMessage();
        if (e.getType() == XMLConfigurationException.NOT_RECOGNIZED) {
            throw new SAXNotRecognizedException(message);
        } else {
            throw new SAXNotSupportedException(message);
        }
    }
}
