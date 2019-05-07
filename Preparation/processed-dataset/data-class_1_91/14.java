/**
	 * This method returns a NotificationInfo object containing the name of the
	 * Java class of the notification and the notification types sent by the
	 * counter monitor.
	 *
	 * @return An Array of MBeanNotificationInfo objects.
	 */
public MBeanNotificationInfo[] getNotificationInfo() {
    MBeanNotificationInfo[] notifInfo = new MBeanNotificationInfo[1];
    String[] types = { "jmx.monitor.error.mbean", "jmx.monitor.error.attribute", "jmx.monitor.error.type", "jmx.monitor.error.runtime" };
    notifInfo[0] = new MBeanNotificationInfo(types, "MonitorErrorNotification", "These types of notification are emitted on Monitor Error Cases");
    return notifInfo;
}
