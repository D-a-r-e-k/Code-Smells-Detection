//}}} 
//{{{ getBuildDate() 
/**
     * Gets the date that jsXe was built as a string.
     * @return a date object for when jsXe was built.
     * @since jsXe 0.4 pre3
     */
public static Date getBuildDate() {
    String buildTime = buildProps.getProperty("build.time");
    //The build date in the build.properties is always US locale. 
    try {
        SimpleDateFormat format = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss z");
        return format.parse(buildTime);
    } catch (ParseException e) {
        Log.log(Log.ERROR, jsXe.class, e);
    }
    return null;
}
