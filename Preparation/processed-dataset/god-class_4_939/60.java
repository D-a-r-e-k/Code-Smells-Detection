public static String formatDefaultTimeStamp(long ts) {
    cal.setTimeInMillis(ts);
    return defaultDateFormat.format(cal.getTime());
}
