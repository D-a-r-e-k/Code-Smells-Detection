public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
    if (!(object instanceof NotificationBroadcaster)) {
        throw new RuntimeOperationsException(new IllegalArgumentException("The MBean does not implement NotificationBroadCaster"));
    }
    ((NotificationBroadcaster) object).removeNotificationListener(listener);
}
