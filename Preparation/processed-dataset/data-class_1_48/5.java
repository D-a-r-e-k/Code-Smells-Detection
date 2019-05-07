/**
    * We are not using the caching, so we just return something that identifies
    * us. This method should never be called directly.
    */
public Serializable getCache() {
    return "JavaGroupsBroadcastingListener: " + bus.getLocalAddress();
}
