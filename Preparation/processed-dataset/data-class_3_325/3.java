/**
     * The configuration string has a number of entries, separated by a ':' (colon).
     * Each entry consists of the name of the protocol, followed by an optional configuration
     * of that protocol. The configuration is enclosed in parentheses, and contains entries
     * which are name/value pairs connected with an assignment sign (=) and separated by
     * a semicolon.
     * <pre>UDP(in_port=5555;out_port=4445):FRAG(frag_size=1024)</pre><p>
     * The <em>first</em> entry defines the <em>bottommost</em> layer, the string is parsed
     * left to right and the protocol stack constructed bottom up. Example: the string
     * "UDP(in_port=5555):FRAG(frag_size=32000):DEBUG" results is the following stack:<pre>
     *
     *   -----------------------
     *  | DEBUG                 |
     *  |-----------------------|
     *  | FRAG frag_size=32000  |
     *  |-----------------------|
     *  | UDP in_port=32000     |
     *   -----------------------
     * </pre>
     */
private static Protocol setupProtocolStack(String configuration, ProtocolStack st) throws Exception {
    Vector<ProtocolConfiguration> protocol_configs = parseConfigurations(configuration);
    Vector<Protocol> protocols = createProtocols(protocol_configs, st);
    if (protocols == null)
        return null;
    // basic protocol sanity check 
    sanityCheck(protocols);
    // check InetAddress related features of stack 
    Map<String, Map<String, InetAddressInfo>> inetAddressMap = createInetAddressMap(protocol_configs, protocols);
    Collection<InetAddress> addrs = getAddresses(inetAddressMap);
    StackType ip_version = Util.getIpStackType();
    // 0 = n/a, 4 = IPv4, 6 = IPv6 
    if (!addrs.isEmpty()) {
        // check that all user-supplied InetAddresses have a consistent version: 
        // 1. If an addr is IPv6 and we have an IPv4 stack --> FAIL 
        // 2. If an address is an IPv4 class D (multicast) address and the stack is IPv6: FAIL 
        // Else pass 
        for (InetAddress addr : addrs) {
            if (addr instanceof Inet6Address && ip_version == StackType.IPv4)
                throw new IllegalArgumentException("found IPv6 address " + addr + " in an IPv4 stack");
            if (addr instanceof Inet4Address && addr.isMulticastAddress() && ip_version == StackType.IPv6)
                throw new Exception("found IPv4 multicast address " + addr + " in an IPv6 stack");
        }
    }
    // process default values 
    setDefaultValues(protocol_configs, protocols, ip_version);
    return connectProtocols(protocols);
}
