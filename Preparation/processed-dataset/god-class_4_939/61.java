public String formatTimeStamp(long ts, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    cal.setTimeInMillis(ts);
    return sdf.format(cal.getTime());
}
