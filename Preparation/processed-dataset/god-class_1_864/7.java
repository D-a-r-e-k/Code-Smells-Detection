// getFeature(String):boolean 
/**
     * Set the value of any property in a SAX2 parser.  The parser
     * might not recognize the property, and if it does recognize
     * it, it might not support the requested value.
     *
     * @param propertyId The unique identifier (URI) of the property
     *                   being set.
     * @param value      The value to which the property is being set.
     *
     * @exception SAXNotRecognizedException If the
     *            requested property is not known.
     * @exception SAXNotSupportedException If the
     *            requested property is known, but the requested
     *            value is not supported.
     */
public void setProperty(String propertyId, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
    try {
        fParserConfiguration.setProperty(propertyId, value);
    } catch (XMLConfigurationException e) {
        String message = e.getMessage();
        if (e.getType() == XMLConfigurationException.NOT_RECOGNIZED) {
            throw new SAXNotRecognizedException(message);
        } else {
            throw new SAXNotSupportedException(message);
        }
    }
}
