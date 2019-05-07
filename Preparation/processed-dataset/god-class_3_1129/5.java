/**
     * Gets the protocol, with default.
     *
     * @return the protocol
     */
public String getProtocol() {
    String protocol = getPropertyAsString(PROTOCOL);
    if (protocol == null || protocol.length() == 0) {
        return DEFAULT_PROTOCOL;
    }
    return protocol;
}
