public void failedArchiving(Long targetInstanceOid, String message) {
    // Update the state.  
    TargetInstance ti = targetInstanceDao.load(targetInstanceOid);
    ti.setState(TargetInstance.STATE_ENDORSED);
    targetInstanceManager.save(ti);
    log.error("Failed to archive - trying to send message");
    // Send a message.  
    inTrayManager.generateNotification(ti.getOwner().getOid(), MessageType.CATEGORY_MISC, "subject.archived.failed", new Object[] { ti.getTarget().getName(), ti.getResourceName() }, "message.archived.failed", new Object[] { ti.getTarget().getName(), ti.getResourceName(), message }, ti, true);
}
