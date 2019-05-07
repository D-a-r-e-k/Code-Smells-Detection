/**
     * Returns the date at which scheduling will start.
     * @return Returns the start date.
     * @hibernate.property type="timestamp" 
     * @hibernate.column name="S_START" not-null="true" sql-type="TIMESTAMP(9)"
     */
public Date getScheduleStartDate() {
    return scheduleStartDate;
}
