/**
	 * Copy the Target
	 * @param aTarget the Target to copy.
	 * @return A copy of the target.
	 */
public Target copy(Target aTarget) {
    Target copy = businessObjectFactory.newTarget();
    copy.setDescription(aTarget.getDescription());
    copy.setProfile(aTarget.getProfile());
    copy.setOverrides(aTarget.getOverrides().copy());
    // Copy the seeds.  
    for (Seed seed : aTarget.getSeeds()) {
        Seed newSeed = businessObjectFactory.newSeed(copy);
        newSeed.setPrimary(seed.isPrimary());
        newSeed.setSeed(seed.getSeed());
        // Copy the related of the permissions.  
        for (Permission perm : seed.getPermissions()) {
            newSeed.addPermission(perm);
        }
        copy.addSeed(newSeed);
    }
    // Copy the schedules.  
    for (Schedule schedule : aTarget.getSchedules()) {
        Schedule newSchedule = businessObjectFactory.newSchedule(copy);
        newSchedule.setCronPattern(schedule.getCronPattern());
        newSchedule.setStartDate(schedule.getStartDate());
        newSchedule.setEndDate(schedule.getEndDate());
        newSchedule.setScheduleType(schedule.getScheduleType());
        copy.addSchedule(newSchedule);
    }
    return copy;
}
