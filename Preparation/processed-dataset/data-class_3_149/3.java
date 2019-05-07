/**
     * Returns the next date/time <I>after</I> the given date/time which does
     * <I>not</I> satisfy the expression
     * 
     * @param date the date/time at which to begin the search for the next 
     *             invalid date/time
     * @return the next valid date/time
     */
public Date getNextInvalidTimeAfter(Date date) {
    long difference = 1000;
    //move back to the nearest second so differences will be accurate 
    Calendar adjustCal = Calendar.getInstance(getTimeZone());
    adjustCal.setTime(date);
    adjustCal.set(Calendar.MILLISECOND, 0);
    Date lastDate = adjustCal.getTime();
    Date newDate = null;
    //TODO: (QUARTZ-481) IMPROVE THIS! The following is a BAD solution to this problem. Performance will be very bad here, depending on the cron expression. It is, however A solution. 
    //keep getting the next included time until it's farther than one second 
    // apart. At that point, lastDate is the last valid fire time. We return 
    // the second immediately following it. 
    while (difference == 1000) {
        newDate = getTimeAfter(lastDate);
        difference = newDate.getTime() - lastDate.getTime();
        if (difference == 1000) {
            lastDate = newDate;
        }
    }
    return new Date(lastDate.getTime() + 1000);
}
