/**
     * @see org.webcurator.core.harvester.coordinator.HarvestAgentListener#notification(Long, String, String)
     */
public void notification(Long aTargetInstanceOid, int notificationCategory, String aMessageType) {
    TargetInstance ti = targetInstanceDao.load(aTargetInstanceOid);
    inTrayManager.generateNotification(ti.getOwner().getOid(), notificationCategory, aMessageType, ti);
}
