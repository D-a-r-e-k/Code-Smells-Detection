/**
     * Determines whether IP host name resolution is done.
     * 
     * @param resolveHosts "true" or "false", if host IP resolution 
     * is desired or not.
     */
public void setResolveHosts(String resolveHosts) {
    this.resolveHosts = new Boolean(resolveHosts).booleanValue();
}
