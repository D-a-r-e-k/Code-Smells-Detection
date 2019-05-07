/**
     * Updates the status of a TargetGroup and its parents. This must be invoked
     * after saving a TargetGroup to ensure that the states of parent groups
     * remain consistent.
     * @param aTargetGroup The TargetGroup to update the state on.
     */
private void updateTargetGroupStatus(TargetGroup aTargetGroup) {
    int originalState = aTargetGroup.getState();
    int newState = originalState;
    // Check if the target group should be expired.  
    Date now = new Date();
    // The TargetGroup has reached it's end date and should be inactive.  
    if (aTargetGroup.getToDate() != null && aTargetGroup.getToDate().before(now)) {
        newState = TargetGroup.STATE_INACTIVE;
    } else {
        boolean allInactive = true;
        newState = Target.STATE_PENDING;
        for (Integer state : targetDao.getSavedMemberStates(aTargetGroup)) {
            if (state != TargetGroup.STATE_INACTIVE) {
                allInactive = false;
            }
            if (state == Target.STATE_APPROVED || state == Target.STATE_COMPLETED || state == TargetGroup.STATE_ACTIVE) {
                newState = TargetGroup.STATE_ACTIVE;
            }
        }
        // If all the children were inactive, we should be inactive too.  
        if (allInactive) {
            newState = TargetGroup.STATE_INACTIVE;
        }
    }
    if (newState == TargetGroup.STATE_ACTIVE) {
        scheduleTargetGroup(aTargetGroup);
    } else {
        log.debug("About to Unschedule the Group");
        unschedule(aTargetGroup);
        log.debug("Unscheduled the group");
    }
    if (originalState != newState) {
        // Save the target and recurse to all parents.  
        aTargetGroup.changeState(newState);
        // Don't bother saving the children; they should already  
        // be saved.  
        targetDao.save(aTargetGroup, false, null);
        // If we're active, schedule us.  
        if (aTargetGroup.getState() == TargetGroup.STATE_ACTIVE) {
            scheduleTargetGroup(aTargetGroup);
        } else {
            unschedule(aTargetGroup);
        }
        // Since my state changed, the state of my parent may  
        // have changed. Recurse to all parents.  
        for (GroupMember parent : aTargetGroup.getParents()) {
            updateTargetGroupStatus(parent.getParent());
        }
    }
}
