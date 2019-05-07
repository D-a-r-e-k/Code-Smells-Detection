// setFeature(String,boolean) 
/**
     * Query the state of a feature.
     *
     * Query the current state of any feature in a SAX2 parser.  The
     * parser might not recognize the feature.
     *
     * @param featureId The unique identifier (URI) of the feature
     *                  being set.
     * @return The current state of the feature.
     * @exception org.xml.sax.SAXNotRecognizedException If the
     *            requested feature is not known.
     * @exception SAXNotSupportedException If the
     *            requested feature is known but not supported.
     */
public boolean getFeature(String featureId) throws SAXNotRecognizedException, SAXNotSupportedException {
    try {
        return fParserConfiguration.getFeature(featureId);
    } catch (XMLConfigurationException e) {
        String message = e.getMessage();
        if (e.getType() == XMLConfigurationException.NOT_RECOGNIZED) {
            throw new SAXNotRecognizedException(message);
        } else {
            throw new SAXNotSupportedException(message);
        }
    }
}
