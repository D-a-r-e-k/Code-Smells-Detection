/**
	 * @see org.webcurator.core.targets.TargetManager#scheduleTargetGroup(org.webcurator.domain.model.core.TargetGroup)
	 */
public void scheduleTargetGroup(TargetGroup aGroup) {
    // Deal with the schedules  
    for (Schedule schedule : aGroup.getSchedules()) {
        //instances.addAll(createTargetInstances(aGroup, schedule, aGroup.getSipType() == TargetGroup.MANY_SIP));  
        processSchedule(schedule);
    }
}
