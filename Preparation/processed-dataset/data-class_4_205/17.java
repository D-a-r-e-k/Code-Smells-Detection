/**
     * Gets the date to end scheduling. 
	 * @return Returns the end date of the schedule.
     * @hibernate.property type="timestamp"
     * @hibernate.column name="S_END" sql-type="TIMESTAMP(9)"
	 */
public Date getScheduleEndDate() {
    return scheduleEndDate;
}
