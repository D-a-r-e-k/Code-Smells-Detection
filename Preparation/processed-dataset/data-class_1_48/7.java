/**
    * A callback that is fired when an existing member leaves the cluster.
    * This method should never be called directly.
    *
    * @param address The address of the member who left.
    */
public void memberLeft(Address address) {
    if (log.isInfoEnabled()) {
        log.info("Member at address '" + address + "' left the cluster");
    }
}
