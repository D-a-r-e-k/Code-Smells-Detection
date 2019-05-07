/**
    * A callback that is fired when a new member joins the cluster. This
    * method should never be called directly.
    *
    * @param address The address of the member who just joined.
    */
public void memberJoined(Address address) {
    if (log.isInfoEnabled()) {
        log.info("A new member at address '" + address + "' has joined the cluster");
    }
}
