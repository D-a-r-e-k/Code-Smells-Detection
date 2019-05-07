public static String formatTimeStamp(long ts, SimpleDateFormat sdf) {
    cal.setTimeInMillis(ts);
    return sdf.format(cal.getTime());
}
