/**
	 * Detect and update TargetGroups that must be made inactive due to their
	 * end date having been passed.
	 */
public void endDateGroups() {
    targetInstanceDao.endDateGroups();
}
