/**
     * get IP source to use - does not apply to Java HTTP implementation currently
     */
public String getIpSource() {
    return getPropertyAsString(IP_SOURCE, "");
}
