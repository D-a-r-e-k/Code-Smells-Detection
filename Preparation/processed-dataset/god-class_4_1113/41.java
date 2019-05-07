/** @todo - overflow */
public static TimestampData addMonths(TimestampData source, int months) {
    int n = source.getNanos();
    synchronized (HsqlDateTime.tempCalGMT) {
        HsqlDateTime.setTimeInMillis(HsqlDateTime.tempCalGMT, source.getSeconds() * 1000);
        HsqlDateTime.tempCalGMT.add(Calendar.MONTH, months);
        TimestampData ts = new TimestampData(HsqlDateTime.tempCalGMT.getTimeInMillis() / 1000, n, source.getZone());
        return ts;
    }
}
