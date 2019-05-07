/**
     * Takes vector of ProtocolConfigurations, iterates through it, creates Protocol for
     * each ProtocolConfiguration and returns all Protocols in a vector.
     * @param protocol_configs Vector of ProtocolConfigurations
     * @param stack The protocol stack
     * @return Vector of Protocols
     */
private static Vector<Protocol> createProtocols(Vector<ProtocolConfiguration> protocol_configs, final ProtocolStack stack) throws Exception {
    Vector<Protocol> retval = new Vector<Protocol>();
    ProtocolConfiguration protocol_config;
    Protocol layer;
    String singleton_name;
    for (int i = 0; i < protocol_configs.size(); i++) {
        protocol_config = protocol_configs.elementAt(i);
        singleton_name = protocol_config.getProperties().get(Global.SINGLETON_NAME);
        if (singleton_name != null && singleton_name.trim().length() > 0) {
            Map<String, Tuple<TP, ProtocolStack.RefCounter>> singleton_transports = ProtocolStack.getSingletonTransports();
            synchronized (singleton_transports) {
                if (i > 0) {
                    // crude way to check whether protocol is a transport 
                    throw new IllegalArgumentException("Property 'singleton_name' can only be used in a transport" + " protocol (was used in " + protocol_config.getProtocolName() + ")");
                }
                Tuple<TP, ProtocolStack.RefCounter> val = singleton_transports.get(singleton_name);
                layer = val != null ? val.getVal1() : null;
                if (layer != null) {
                    retval.add(layer);
                } else {
                    layer = protocol_config.createLayer(stack);
                    if (layer == null)
                        return null;
                    singleton_transports.put(singleton_name, new Tuple<TP, ProtocolStack.RefCounter>((TP) layer, new ProtocolStack.RefCounter((short) 0, (short) 0)));
                    retval.addElement(layer);
                }
            }
            continue;
        }
        layer = protocol_config.createLayer(stack);
        if (layer == null)
            return null;
        retval.addElement(layer);
    }
    return retval;
}
