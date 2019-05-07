public String convertToString(Object a) {
    boolean zone = false;
    String s;
    StringBuffer sb;
    if (a == null) {
        return null;
    }
    switch(typeCode) {
        case Types.SQL_DATE:
            return HsqlDateTime.getDateString(((TimestampData) a).getSeconds());
        case Types.SQL_TIME_WITH_TIME_ZONE:
        case Types.SQL_TIME:
            {
                TimeData t = (TimeData) a;
                int seconds = normaliseTime(t.getSeconds() + t.getZone());
                s = intervalSecondToString(seconds, t.getNanos(), false);
                if (!withTimeZone) {
                    return s;
                }
                sb = new StringBuffer(s);
                s = Type.SQL_INTERVAL_HOUR_TO_MINUTE.intervalSecondToString(((TimeData) a).getZone(), 0, true);
                sb.append(s);
                return sb.toString();
            }
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
        case Types.SQL_TIMESTAMP:
            {
                TimestampData ts = (TimestampData) a;
                sb = new StringBuffer();
                HsqlDateTime.getTimestampString(sb, ts.getSeconds() + ts.getZone(), ts.getNanos(), scale);
                if (!withTimeZone) {
                    return sb.toString();
                }
                s = Type.SQL_INTERVAL_HOUR_TO_MINUTE.intervalSecondToString(((TimestampData) a).getZone(), 0, true);
                sb.append(s);
                return sb.toString();
            }
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
    }
}
