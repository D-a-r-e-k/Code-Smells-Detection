/** 
	 * Copy a TargetGroup.
	 * @param aTargetGroup The target group to copy.
	 * @return a copy of the gropu.
	 */
public TargetGroup copy(TargetGroup aTargetGroup) {
    TargetGroup copy = businessObjectFactory.newTargetGroup();
    copy.setName(trunc("Copy of " + aTargetGroup.getName(), AbstractTarget.CNST_MAX_NAME_LENGTH));
    copy.setDescription(aTargetGroup.getDescription());
    // Copy the schedules.  
    for (Schedule schedule : aTargetGroup.getSchedules()) {
        Schedule newSchedule = businessObjectFactory.newSchedule(copy);
        newSchedule.setCronPattern(schedule.getCronPattern());
        newSchedule.setStartDate(schedule.getStartDate());
        newSchedule.setEndDate(schedule.getEndDate());
        newSchedule.setScheduleType(schedule.getScheduleType());
        copy.addSchedule(newSchedule);
    }
    // Copy the profile settings  
    copy.setProfile(aTargetGroup.getProfile());
    copy.setOverrides(aTargetGroup.getOverrides().copy());
    // Copy child references.  
    for (GroupMember gm : aTargetGroup.getChildren()) {
        copy.getNewChildren().add(new GroupMemberDTO(copy, gm.getChild()));
    }
    return copy;
}
