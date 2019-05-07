/**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentListener#notification(String, String)
     */
public void notification(String aSubject, int notificationCategory, String aMessage) {
    List<String> privs = new ArrayList<String>();
    privs.add(Privilege.MANAGE_WEB_HARVESTER);
    inTrayManager.generateNotification(privs, notificationCategory, aSubject, aMessage);
}
