/*
     * A method which does the following:
     * - discovers all Fields or Methods within the protocol stack which set
     * InetAddress, IpAddress, InetSocketAddress (and Lists of such) for which the user *has*
     * specified a default value.
	 * - stores the resulting set of Fields and Methods in a map of the form:
     *  Protocol -> Property -> InetAddressInfo 
     * where InetAddressInfo instances encapsulate the InetAddress related information 
     * of the Fields and Methods.
     */
public static Map<String, Map<String, InetAddressInfo>> createInetAddressMap(Vector<ProtocolConfiguration> protocol_configs, Vector<Protocol> protocols) throws Exception {
    // Map protocol -> Map<String, InetAddressInfo>, where the latter is protocol specific 
    Map<String, Map<String, InetAddressInfo>> inetAddressMap = new HashMap<String, Map<String, InetAddressInfo>>();
    // collect InetAddressInfo 
    for (int i = 0; i < protocol_configs.size(); i++) {
        ProtocolConfiguration protocol_config = protocol_configs.get(i);
        Protocol protocol = protocols.get(i);
        String protocolName = protocol.getName();
        // regenerate the Properties which were destroyed during basic property processing 
        Map<String, String> properties = protocol_config.getOriginalProperties();
        // check which InetAddress-related properties are ***non-null ***, and 
        // create an InetAddressInfo structure for them 
        // Method[] methods=protocol.getClass().getMethods(); 
        Method[] methods = Util.getAllDeclaredMethodsWithAnnotations(protocol.getClass(), Property.class);
        for (int j = 0; j < methods.length; j++) {
            if (methods[j].isAnnotationPresent(Property.class) && isSetPropertyMethod(methods[j])) {
                String propertyName = PropertyHelper.getPropertyName(methods[j]);
                String propertyValue = properties.get(propertyName);
                // if there is a systemProperty attribute defined in the annotation, set the property value from the system property 
                String tmp = grabSystemProp(methods[j].getAnnotation(Property.class));
                if (tmp != null)
                    propertyValue = tmp;
                if (propertyValue != null && InetAddressInfo.isInetAddressRelated(methods[j])) {
                    Object converted = null;
                    try {
                        converted = PropertyHelper.getConvertedValue(protocol, methods[j], properties, propertyValue, false);
                    } catch (Exception e) {
                        throw new Exception("String value could not be converted for method " + propertyName + " in " + protocolName + " with default value " + propertyValue + ".Exception is " + e, e);
                    }
                    InetAddressInfo inetinfo = new InetAddressInfo(protocol, methods[j], properties, propertyValue, converted);
                    Map<String, InetAddressInfo> protocolInetAddressMap = inetAddressMap.get(protocolName);
                    if (protocolInetAddressMap == null) {
                        protocolInetAddressMap = new HashMap<String, InetAddressInfo>();
                        inetAddressMap.put(protocolName, protocolInetAddressMap);
                    }
                    protocolInetAddressMap.put(propertyName, inetinfo);
                }
            }
        }
        //traverse class hierarchy and find all annotated fields and add them to the list if annotated 
        for (Class<?> clazz = protocol.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            Field[] fields = clazz.getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                if (fields[j].isAnnotationPresent(Property.class)) {
                    String propertyName = PropertyHelper.getPropertyName(fields[j], properties);
                    String propertyValue = properties.get(propertyName);
                    // if there is a systemProperty attribute defined in the annotation, set the property value from the system property 
                    String tmp = grabSystemProp(fields[j].getAnnotation(Property.class));
                    if (tmp != null)
                        propertyValue = tmp;
                    if ((propertyValue != null || !PropertyHelper.usesDefaultConverter(fields[j])) && InetAddressInfo.isInetAddressRelated(protocol, fields[j])) {
                        Object converted = null;
                        try {
                            converted = PropertyHelper.getConvertedValue(protocol, fields[j], properties, propertyValue, false);
                        } catch (Exception e) {
                            throw new Exception("String value could not be converted for method " + propertyName + " in " + protocolName + " with default value " + propertyValue + ".Exception is " + e, e);
                        }
                        InetAddressInfo inetinfo = new InetAddressInfo(protocol, fields[j], properties, propertyValue, converted);
                        Map<String, InetAddressInfo> protocolInetAddressMap = inetAddressMap.get(protocolName);
                        if (protocolInetAddressMap == null) {
                            protocolInetAddressMap = new HashMap<String, InetAddressInfo>();
                            inetAddressMap.put(protocolName, protocolInetAddressMap);
                        }
                        protocolInetAddressMap.put(propertyName, inetinfo);
                    }
                }
            }
        }
    }
    return inetAddressMap;
}
