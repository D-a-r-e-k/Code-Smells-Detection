/**
     * Returns all inet addresses found
     */
public static Collection<InetAddress> getAddresses(Map<String, Map<String, InetAddressInfo>> inetAddressMap) throws Exception {
    Set<InetAddress> addrs = new HashSet<InetAddress>();
    for (Map.Entry<String, Map<String, InetAddressInfo>> inetAddressMapEntry : inetAddressMap.entrySet()) {
        Map<String, InetAddressInfo> protocolInetAddressMap = inetAddressMapEntry.getValue();
        for (Map.Entry<String, InetAddressInfo> protocolInetAddressMapEntry : protocolInetAddressMap.entrySet()) {
            InetAddressInfo inetAddressInfo = protocolInetAddressMapEntry.getValue();
            // add InetAddressInfo to sets based on IP version 
            List<InetAddress> addresses = inetAddressInfo.getInetAddresses();
            for (InetAddress address : addresses) {
                if (address == null)
                    throw new RuntimeException("This address should not be null! - something is wrong");
                addrs.add(address);
            }
        }
    }
    return addrs;
}
