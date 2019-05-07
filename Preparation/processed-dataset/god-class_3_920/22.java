public MBeanNotificationInfo[] getNotificationInfo() {
    if (!(object instanceof NotificationBroadcaster))
        return null;
    return ((NotificationBroadcaster) object).getNotificationInfo();
}
