/**
	 * Sets the schedules cron pattern.
	 * Private as this should only be called from Hibernate.
	 * @param aScheduleCronPattern The scheduleCronPattern to set. 
	 */
@SuppressWarnings("unused")
private void setScheduleCronPattern(String aScheduleCronPattern) {
    scheduleCronPattern = aScheduleCronPattern;
}
