/**
     * Indicates whether the specified cron expression can be parsed into a 
     * valid cron expression
     * 
     * @param cronExpression the expression to evaluate
     * @return a boolean indicating whether the given expression is a valid cron
     *         expression
     */
public static boolean isValidExpression(String cronExpression) {
    try {
        new CronExpression(cronExpression);
    } catch (ParseException pe) {
        return false;
    }
    return true;
}
