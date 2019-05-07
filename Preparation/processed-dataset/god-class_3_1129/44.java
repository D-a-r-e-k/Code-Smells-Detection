/**
     * Get the port; apply the default for the protocol if necessary.
     *
     * @return the port number, with default applied if required.
     */
public int getPort() {
    final int port = getPortIfSpecified();
    if (port == UNSPECIFIED_PORT) {
        String prot = getProtocol();
        if (HTTPConstants.PROTOCOL_HTTPS.equalsIgnoreCase(prot)) {
            return HTTPConstants.DEFAULT_HTTPS_PORT;
        }
        if (!HTTPConstants.PROTOCOL_HTTP.equalsIgnoreCase(prot)) {
            log.warn("Unexpected protocol: " + prot);
        }
        return HTTPConstants.DEFAULT_HTTP_PORT;
    }
    return port;
}
