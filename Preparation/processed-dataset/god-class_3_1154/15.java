/**
     * This method takes a set of InetAddresses, represented by an inetAddressmap, and:
     * - if the resulting set is non-empty, goes through to see if all InetAddress-related
     * user settings have a consistent IP version: v4 or v6, and throws an exception if not
     * - if the resulting set is empty, sets the default IP version based on available stacks
     * and if a dual stack, stack preferences
     * - sets the IP version to be used in the JGroups session
     * @return StackType.IPv4 for IPv4, StackType.IPv6 for IPv6, StackType.Unknown if the version cannot be determined
     */
public static StackType determineIpVersionFromAddresses(Collection<InetAddress> addrs) throws Exception {
    Set<InetAddress> ipv4_addrs = new HashSet<InetAddress>();
    Set<InetAddress> ipv6_addrs = new HashSet<InetAddress>();
    for (InetAddress address : addrs) {
        if (address instanceof Inet4Address)
            ipv4_addrs.add(address);
        else
            ipv6_addrs.add(address);
    }
    if (log.isTraceEnabled())
        log.trace("all addrs=" + addrs + ", IPv4 addrs=" + ipv4_addrs + ", IPv6 addrs=" + ipv6_addrs);
    // the user supplied 1 or more IP address inputs. Check if we have a consistent set 
    if (!addrs.isEmpty()) {
        if (!ipv4_addrs.isEmpty() && !ipv6_addrs.isEmpty()) {
            throw new RuntimeException("all addresses have to be either IPv4 or IPv6: IPv4 addresses=" + ipv4_addrs + ", IPv6 addresses=" + ipv6_addrs);
        }
        return !ipv6_addrs.isEmpty() ? StackType.IPv6 : StackType.IPv4;
    }
    return StackType.Unknown;
}
