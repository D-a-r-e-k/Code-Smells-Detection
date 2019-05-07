// getErrorHandler():ErrorHandler 
/**
     * Set the state of any feature in a SAX2 parser.  The parser
     * might not recognize the feature, and if it does recognize
     * it, it might not be able to fulfill the request.
     *
     * @param featureId The unique identifier (URI) of the feature.
     * @param state The requested state of the feature (true or false).
     *
     * @exception SAXNotRecognizedException If the
     *            requested feature is not known.
     * @exception SAXNotSupportedException If the
     *            requested feature is known, but the requested
     *            state is not supported.
     */
public void setFeature(String featureId, boolean state) throws SAXNotRecognizedException, SAXNotSupportedException {
    try {
        fParserConfiguration.setFeature(featureId, state);
    } catch (XMLConfigurationException e) {
        String message = e.getMessage();
        if (e.getType() == XMLConfigurationException.NOT_RECOGNIZED) {
            throw new SAXNotRecognizedException(message);
        } else {
            throw new SAXNotSupportedException(message);
        }
    }
}
