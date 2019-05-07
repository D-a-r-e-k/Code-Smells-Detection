/**
     * Get the port number from the port string, allowing for trailing blanks.
     *
     * @return port number or UNSPECIFIED_PORT (== 0)
     */
public int getPortIfSpecified() {
    String port_s = getPropertyAsString(PORT, UNSPECIFIED_PORT_AS_STRING);
    try {
        return Integer.parseInt(port_s.trim());
    } catch (NumberFormatException e) {
        return UNSPECIFIED_PORT;
    }
}
