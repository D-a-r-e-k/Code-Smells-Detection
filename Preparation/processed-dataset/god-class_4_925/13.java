/**
     * Sets the port for the QuickServer to listen on.
	 * If not set, it will run on Port 9876 
	 * @param port to listen on.
     * @see #getPort
     */
public void setPort(int port) {
    if (port < 0) {
        throw new IllegalArgumentException("Port number can not be less than 0!");
    }
    serverPort = port;
    logger.finest("Set to " + port);
}
