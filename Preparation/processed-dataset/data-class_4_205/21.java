/**
	 * Returns the schedules cron pattern
	 * @return Returns the schedules cron pattern.
	 * @hibernate.property column="S_CRON" length="255"
	 */
public String getScheduleCronPattern() {
    return scheduleCronPattern;
}
