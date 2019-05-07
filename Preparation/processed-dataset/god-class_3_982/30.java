public Object clone() {
    CronExpression copy = null;
    try {
        copy = new CronExpression(getCronExpression());
        if (getTimeZone() != null)
            copy.setTimeZone((TimeZone) getTimeZone().clone());
    } catch (ParseException ex) {
        // never happens since the source is valid... 
        throw new IncompatibleClassChangeError("Not Cloneable.");
    }
    return copy;
}
