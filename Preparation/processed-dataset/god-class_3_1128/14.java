/**
     * 
     */
private void closeThreadLocalConnections() {
    // Does not need to be synchronised, as all access is from same thread 
    Map<HostConfiguration, HttpClient> map = httpClients.get();
    if (map != null) {
        for (HttpClient cl : map.values()) {
            // Can cause NPE in HttpClient 3.1 
            //((SimpleHttpConnectionManager)cl.getHttpConnectionManager()).shutdown();// Closes the connection 
            // Revert to original method: 
            cl.getHttpConnectionManager().closeIdleConnections(-1000);
        }
        map.clear();
    }
}
