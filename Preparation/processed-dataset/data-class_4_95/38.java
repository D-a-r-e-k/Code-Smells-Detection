/**
	 * Sets the Ip address to bind to. 
	 * @param bindAddr argument can be used on a multi-homed host for a 
	 * QuickServer that will only accept connect requests to one 
	 * of its addresses. If not set, it will default accepting 
	 * connections on any/all local addresses.
	 * @exception java.net.UnknownHostException if no IP address for 
	 * the host could be found
	 * @since 1.1
	 * @see #getBindAddr
	 */
public void setBindAddr(String bindAddr) throws UnknownHostException {
    ipAddr = InetAddress.getByName(bindAddr);
    logger.finest("Set to " + bindAddr);
}
