/**
	 * Returns the IP address binding to. 
	 * @since 1.1
	 * @see #setBindAddr
	 */
public InetAddress getBindAddr() {
    if (ipAddr == null) {
        try {
            ipAddr = InetAddress.getByName("0.0.0.0");
        } catch (Exception e) {
            logger.warning("Unable to create default ip(0.0.0.0) : " + e);
            throw new RuntimeException("Error: Unable to find servers own ip : " + e);
        }
    }
    return ipAddr;
}
