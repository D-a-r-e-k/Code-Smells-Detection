/** @todo - check the time zone conversion */
public Object convertJavaToSQL(SessionInterface session, Object a) {
    if (a == null) {
        return null;
    }
    switch(typeCode) {
        case Types.SQL_TIME:
        case Types.SQL_TIME_WITH_TIME_ZONE:
            if (a instanceof java.sql.Date) {
                break;
            }
            if (a instanceof java.util.Date) {
                long millis;
                int nanos = 0;
                int zoneSeconds = 0;
                if (typeCode == Types.SQL_TIME) {
                    millis = HsqlDateTime.convertMillisFromCalendar(session.getCalendar(), ((java.util.Date) a).getTime());
                } else {
                    millis = ((java.util.Date) a).getTime();
                    zoneSeconds = session.getZoneSeconds();
                }
                millis = HsqlDateTime.getNormalisedTime(millis);
                if (a instanceof java.sql.Timestamp) {
                    nanos = ((java.sql.Timestamp) a).getNanos();
                    nanos = normaliseFraction(nanos, scale);
                }
                return new TimeData((int) millis / 1000, nanos, zoneSeconds);
            }
            break;
        case Types.SQL_DATE:
            {
                if (a instanceof java.sql.Time) {
                    break;
                }
                if (a instanceof java.util.Date) {
                    long millis;
                    millis = HsqlDateTime.convertMillisFromCalendar(session.getCalendar(), ((java.util.Date) a).getTime());
                    millis = HsqlDateTime.getNormalisedDate(millis);
                    return new TimestampData(millis / 1000);
                }
                break;
            }
        case Types.SQL_TIMESTAMP:
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            {
                if (a instanceof java.sql.Time) {
                    break;
                }
                if (a instanceof java.util.Date) {
                    long millis;
                    int nanos = 0;
                    int zoneSeconds = 0;
                    if (typeCode == Types.SQL_TIMESTAMP) {
                        millis = HsqlDateTime.convertMillisFromCalendar(session.getCalendar(), ((java.util.Date) a).getTime());
                    } else {
                        millis = ((java.util.Date) a).getTime();
                        zoneSeconds = session.getZoneSeconds();
                    }
                    if (a instanceof java.sql.Timestamp) {
                        nanos = ((java.sql.Timestamp) a).getNanos();
                        nanos = this.normaliseFraction(nanos, scale);
                    }
                    return new TimestampData(millis / 1000, nanos, zoneSeconds);
                }
                break;
            }
    }
    throw Error.error(ErrorCode.X_42561);
}
