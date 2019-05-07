//IMplementing for NotificationListener 
public void handleNotification(Notification notification, Object handback) {
    if (!(object instanceof NotificationListener))
        return;
    ((NotificationListener) object).handleNotification(notification, handback);
}
