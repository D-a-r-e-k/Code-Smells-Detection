/**
     * Returns the port for the QuickServer.
     * @see #setPort
     */
public int getPort() {
    if (isClosed() == false) {
        return server.getLocalPort();
    }
    if (getSecure().isEnable() == false) {
        return serverPort;
    } else {
        int _port = getSecure().getPort();
        if (_port == -1)
            return serverPort;
        else
            return _port;
    }
}
