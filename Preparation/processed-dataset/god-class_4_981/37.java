/**
     * Get whether the threads spawned by this JobStore should be
     * marked as daemon.  Possible threads include the <code>MisfireHandler</code> 
     * and the <code>ClusterManager</code>.
     * 
     * @see Thread#setDaemon(boolean)
     */
public boolean getMakeThreadsDaemons() {
    return makeThreadsDaemons;
}
