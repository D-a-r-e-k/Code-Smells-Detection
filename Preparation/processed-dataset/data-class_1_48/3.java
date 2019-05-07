/**
    * Uses JavaGroups to broadcast the supplied notification message across the cluster.
    *
    * @param message The cluster nofication message to broadcast.
    */
protected void sendNotification(ClusterNotification message) {
    bus.sendNotification(message);
}
