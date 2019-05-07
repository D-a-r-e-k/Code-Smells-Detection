/**
     * Returns the time zone for which this <code>CronExpression</code> 
     * will be resolved.
     */
public TimeZone getTimeZone() {
    if (timeZone == null) {
        timeZone = TimeZone.getDefault();
    }
    return timeZone;
}
