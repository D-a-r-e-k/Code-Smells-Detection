/**
     * Indicates whether the given date satisfies the cron expression. Note that
     * milliseconds are ignored, so two Dates falling on different milliseconds
     * of the same second will always have the same result here.
     * 
     * @param date the date to evaluate
     * @return a boolean indicating whether the given date satisfies the cron
     *         expression
     */
public boolean isSatisfiedBy(Date date) {
    Calendar testDateCal = Calendar.getInstance(getTimeZone());
    testDateCal.setTime(date);
    testDateCal.set(Calendar.MILLISECOND, 0);
    Date originalDate = testDateCal.getTime();
    testDateCal.add(Calendar.SECOND, -1);
    Date timeAfter = getTimeAfter(testDateCal.getTime());
    return ((timeAfter != null) && (timeAfter.equals(originalDate)));
}
