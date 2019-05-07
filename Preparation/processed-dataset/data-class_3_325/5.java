/* ------------------------------- Private Methods ------------------------------------- */
/**
     * Creates a protocol stack by iterating through the protocol list and connecting
     * adjacent layers. The list starts with the topmost layer and has the bottommost
     * layer at the tail.
     * @param protocol_list List of Protocol elements (from top to bottom)
     * @return Protocol stack
     */
private static Protocol connectProtocols(Vector<Protocol> protocol_list) {
    Protocol current_layer = null, next_layer = null;
    for (int i = 0; i < protocol_list.size(); i++) {
        current_layer = protocol_list.elementAt(i);
        if (i + 1 >= protocol_list.size())
            break;
        next_layer = protocol_list.elementAt(i + 1);
        next_layer.setDownProtocol(current_layer);
        current_layer.setUpProtocol(next_layer);
        if (current_layer instanceof TP) {
            TP transport = (TP) current_layer;
            if (transport.isSingleton()) {
                ConcurrentMap<String, Protocol> up_prots = transport.getUpProtocols();
                String key;
                synchronized (up_prots) {
                    while (true) {
                        key = Global.DUMMY + System.currentTimeMillis();
                        if (up_prots.containsKey(key))
                            continue;
                        up_prots.put(key, next_layer);
                        break;
                    }
                }
                current_layer.setUpProtocol(null);
            }
        }
    }
    return current_layer;
}
