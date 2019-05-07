//Implementing NotificationBroadcaster 
public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
    if (!(object instanceof NotificationBroadcaster)) {
        throw new RuntimeOperationsException(new IllegalArgumentException("The MBean does not implement NotificationBroadCaster"));
    }
    ((NotificationBroadcaster) object).addNotificationListener(listener, filter, handback);
}
