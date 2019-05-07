/**
	 * Unschedule the target by deleting all scheduled instances 
	 * related to its schedules.
	 * @param aTarget
	 */
private void unschedule(AbstractTarget aTarget) {
    targetInstanceDao.deleteScheduledInstances(aTarget);
}
