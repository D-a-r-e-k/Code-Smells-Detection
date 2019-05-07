//Implementing for NotificationFilter 
public boolean isNotificationEnabled(Notification notification) {
    if (!(object instanceof NotificationFilter))
        return false;
    return ((NotificationFilter) object).isNotificationEnabled(notification);
}
