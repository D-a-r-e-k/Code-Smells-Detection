/**
	 * Process a batch schedule and calculate all of its target instances.
	 * @param aSchedule The schedule to evaluate.
	 * @return A Set of Target instances.
	 */
public void processBatchSchedule(Schedule aSchedule) {
    AbstractTarget aTarget = aSchedule.getTarget();
    boolean checkAgency = false;
    TargetGroup group = null;
    Hibernate.initialize(aTarget);
    if (aTarget.getObjectType() == AbstractTarget.TYPE_GROUP) {
        log.debug(" Schedules target is a group.");
        if (aTarget instanceof TargetGroup) {
            group = (TargetGroup) aTarget;
        } else {
            group = targetDao.loadGroup(aTarget.getOid(), true);
        }
        if (group.getSipType() == TargetGroup.MANY_SIP) {
            checkAgency = true;
        }
    } else {
        log.debug(" Schedules target is a target.");
    }
    createBatchTargetInstances(aTarget, aSchedule, group, checkAgency);
    // Get the schedule ahead time from our environment.  
    int daysToSchedule = EnvironmentFactory.getEnv().getDaysToSchedule();
    // Determine when to end the schedule. This is the earliest of the   
    // end date, or the current date + the number of days ahead to schedule.  
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DAY_OF_MONTH, daysToSchedule);
    cal.add(Calendar.SECOND, -1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(new Date());
    Date firstAfterPeriod = aSchedule.getNextExecutionDate(cal.getTime());
    if (firstAfterPeriod != null) {
        aSchedule.setNextScheduleAfterPeriod(firstAfterPeriod);
    }
    aSchedule.setLastProcessedDate(cal2.getTime());
    targetDao.save(aSchedule);
    log.debug(" Saved schedule: " + aSchedule.getOid() + " - set last processed date to: " + cal2.getTime());
}
