/**
     * Specifies an output format for this serializer. It the
     * serializer has already been associated with an output format,
     * it will switch to the new format. This method should not be
     * called while the serializer is in the process of serializing
     * a document.
     *
     * @param format The output format to use
     */
public void setOutputFormat(Properties format) {
    boolean shouldFlush = m_shouldFlush;
    if (format != null) {
        // Set the default values first,  
        // and the non-default values after that,   
        // just in case there is some unexpected  
        // residual values left over from over-ridden default values  
        Enumeration propNames;
        propNames = format.propertyNames();
        while (propNames.hasMoreElements()) {
            String key = (String) propNames.nextElement();
            // Get the value, possibly a default value  
            String value = format.getProperty(key);
            // Get the non-default value (if any).  
            String explicitValue = (String) format.get(key);
            if (explicitValue == null && value != null) {
                // This is a default value  
                this.setOutputPropertyDefault(key, value);
            }
            if (explicitValue != null) {
                // This is an explicit non-default value  
                this.setOutputProperty(key, explicitValue);
            }
        }
    }
    // Access this only from the Hashtable level... we don't want to   
    // get default properties.  
    String entitiesFileName = (String) format.get(OutputPropertiesFactory.S_KEY_ENTITIES);
    if (null != entitiesFileName) {
        String method = (String) format.get(OutputKeys.METHOD);
        m_charInfo = CharInfo.getCharInfo(entitiesFileName, method);
    }
    m_shouldFlush = shouldFlush;
}
