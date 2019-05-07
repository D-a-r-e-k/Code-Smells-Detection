public BigDecimal getSecondPart(Object dateTime) {
    long seconds = getPart(null, dateTime, Types.SQL_INTERVAL_SECOND);
    int nanos = 0;
    if (typeCode == Types.SQL_TIMESTAMP) {
        nanos = ((TimestampData) dateTime).getNanos();
    } else if (typeCode == Types.SQL_TIME) {
        nanos = ((TimeData) dateTime).getNanos();
    }
    return getSecondPart(seconds, nanos);
}
