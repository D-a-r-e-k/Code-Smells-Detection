/**
     * Tell whether the default port for the specified protocol is used
     *
     * @return true if the default port number for the protocol is used, false otherwise
     */
public boolean isProtocolDefaultPort() {
    final int port = getPortIfSpecified();
    final String protocol = getProtocol();
    if (port == UNSPECIFIED_PORT || (HTTPConstants.PROTOCOL_HTTP.equalsIgnoreCase(protocol) && port == HTTPConstants.DEFAULT_HTTP_PORT) || (HTTPConstants.PROTOCOL_HTTPS.equalsIgnoreCase(protocol) && port == HTTPConstants.DEFAULT_HTTPS_PORT)) {
        return true;
    }
    return false;
}
