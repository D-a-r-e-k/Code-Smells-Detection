/**
     * Returns the FreeColGameObjectType identified by the
     * attributeName, or the default value if there is no such
     * attribute.
     *
     * @param in the XMLStreamReader
     * @param attributeName the name of the attribute identifying the
     * FreeColGameObjectType
     * @param returnClass the class of the return value
     * @param defaultValue the value to return if there is no
     * attribute named attributeName
     * @return a FreeColGameObjectType value
     */
public <T extends FreeColGameObjectType> T getType(XMLStreamReader in, String attributeName, Class<T> returnClass, T defaultValue) {
    final String attributeString = in.getAttributeValue(null, attributeName);
    if (attributeString != null) {
        return getType(attributeString, returnClass);
    } else {
        return defaultValue;
    }
}
