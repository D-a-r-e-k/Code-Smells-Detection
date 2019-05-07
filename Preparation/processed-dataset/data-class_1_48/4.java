/**
    * Handles incoming notification messages from JavaGroups. This method should
    * never be called directly.
    *
    * @param serializable The incoming message object. This must be a {@link ClusterNotification}.
    */
public void handleNotification(Serializable serializable) {
    if (!(serializable instanceof ClusterNotification)) {
        log.error("An unknown cluster notification message received (class=" + serializable.getClass().getName() + "). Notification ignored.");
        return;
    }
    handleClusterNotification((ClusterNotification) serializable);
}
