public void completeArchiving(Long targetInstanceOid, String archiveIID) {
    // Update the state.  
    TargetInstance ti = targetInstanceDao.load(targetInstanceOid);
    ti.setArchiveIdentifier(archiveIID);
    ti.setArchivedTime(new Date());
    ti.setState(TargetInstance.STATE_ARCHIVED);
    targetInstanceManager.save(ti);
    //Remove any indexes  
    removeIndexes(ti);
    // Send a message.  
    inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.NOTIFICATION_ARCHIVE_SUCCESS, ti);
}
