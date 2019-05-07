/**
     * <p>
     * Get the frequency (in milliseconds) at which this instance "checks-in"
     * with the other instances of the cluster. -- Affects the rate of
     * detecting failed instances.
     * </p>
     */
public long getClusterCheckinInterval() {
    return clusterCheckinInterval;
}
