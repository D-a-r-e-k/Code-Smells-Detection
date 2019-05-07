/**
	 * Process a schedule and calculate all of its target instances.
	 * @param aSchedule The schedule to evaluate.
	 * @return A Set of Target instances.
	 */
public void processSchedule(Schedule aSchedule) {
    AbstractTarget aTarget = aSchedule.getTarget();
    boolean checkAgency = false;
    if (aTarget.getObjectType() == AbstractTarget.TYPE_GROUP) {
        TargetGroup group;
        if (aTarget instanceof TargetGroup) {
            group = (TargetGroup) aTarget;
        } else {
            group = targetDao.loadGroup(aTarget.getOid());
        }
        if (group.getSipType() == TargetGroup.MANY_SIP) {
            checkAgency = true;
        }
    }
    createTargetInstances(aSchedule.getTarget(), aSchedule, checkAgency);
    // Get the schedule ahead time from our environment.  
    int daysToSchedule = EnvironmentFactory.getEnv().getDaysToSchedule();
    // Determine when to end the schedule. This is the earliest of the   
    // end date, or the current date + the number of days ahead to schedule.  
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DAY_OF_MONTH, daysToSchedule);
    cal.add(Calendar.SECOND, -1);
    Date firstAfterPeriod = aSchedule.getNextExecutionDate(cal.getTime());
    if (firstAfterPeriod != null) {
        aSchedule.setNextScheduleAfterPeriod(firstAfterPeriod);
        targetDao.save(aSchedule);
    }
}
