/**
     * Returns the next date/time <I>after</I> the given date/time which
     * satisfies the cron expression.
     * 
     * @param date the date/time at which to begin the search for the next valid
     *             date/time
     * @return the next valid date/time
     */
public Date getNextValidTimeAfter(Date date) {
    return getTimeAfter(date);
}
