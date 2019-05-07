/**
	 * Returns the schedule type (CUSTOM_SCHEDULE, TYPE_DAILY, etc).
	 * @return the schedule type (CUSTOM_SCHEDULE, TYPE_DAILY, etc) a positive or negative integer.
	 * @hibernate.property column="S_TYPE"
	 */
public int getScheduleType() {
    return scheduleType;
}
