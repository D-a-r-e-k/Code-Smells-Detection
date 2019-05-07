/**
     * Advance the calendar to the particular hour paying particular attention
     * to daylight saving problems.
     * 
     * @param cal
     * @param hour
     */
protected void setCalendarHour(Calendar cal, int hour) {
    cal.set(java.util.Calendar.HOUR_OF_DAY, hour);
    if (cal.get(java.util.Calendar.HOUR_OF_DAY) != hour && hour != 24) {
        cal.set(java.util.Calendar.HOUR_OF_DAY, hour + 1);
    }
}
