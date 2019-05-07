/**
	 * Check that the target that the instance belongs to is approved and if not dont harvest.
	 * @param aTargetInstance the target instance whos target should be checked.
	 * @return flag to indicat approval
	 */
private boolean isTargetApproved(TargetInstance aTargetInstance) {
    // Check permissions if none defer the target instance and send and notification  
    if (!targetManager.isTargetHarvestable(aTargetInstance)) {
        // Defer the schedule 24 hours and notifiy the owner.  
        Calendar cal = Calendar.getInstance();
        cal.setTime(aTargetInstance.getScheduledTime());
        cal.add(Calendar.DATE, 1);
        aTargetInstance.setScheduledTime(cal.getTime());
        targetInstanceDao.save(aTargetInstance);
        if (log.isInfoEnabled()) {
            log.info("The target " + aTargetInstance.getTarget().getName() + " is not apporoved for harvest and has been defered 24 hours.");
        }
        inTrayManager.generateNotification(aTargetInstance.getOwner().getOid(), MessageType.CATEGORY_MISC, MessageType.TARGET_INSTANCE_RESCHEDULED, aTargetInstance);
        return false;
    }
    return true;
}
