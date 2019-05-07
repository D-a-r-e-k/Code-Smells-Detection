/**
     * Return a number of ProtocolConfigurations in a vector
     * @param configuration protocol-stack configuration string
     * @return Vector of ProtocolConfigurations
     */
public static Vector<ProtocolConfiguration> parseConfigurations(String configuration) throws Exception {
    Vector<ProtocolConfiguration> retval = new Vector<ProtocolConfiguration>();
    Vector<String> protocol_string = parseProtocols(configuration);
    if (protocol_string == null)
        return null;
    for (String component_string : protocol_string) {
        retval.addElement(new ProtocolConfiguration(component_string));
    }
    return retval;
}
