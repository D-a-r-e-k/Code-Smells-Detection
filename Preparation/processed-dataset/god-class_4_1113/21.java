public Object convertSQLToJava(SessionInterface session, Object a) {
    if (a == null) {
        return null;
    }
    switch(typeCode) {
        case Types.SQL_TIME:
            {
                Calendar cal = session.getCalendar();
                long millis = HsqlDateTime.convertMillisToCalendar(cal, ((TimeData) a).getSeconds() * 1000);
                millis = HsqlDateTime.getNormalisedTime(cal, millis);
                java.sql.Time value = new java.sql.Time(millis);
                return value;
            }
        case Types.SQL_TIME_WITH_TIME_ZONE:
            {
                int seconds = ((TimeData) a).getSeconds();
                return new java.sql.Time(seconds * 1000);
            }
        case Types.SQL_DATE:
            {
                Calendar cal = session.getCalendar();
                long millis = HsqlDateTime.convertMillisToCalendar(cal, ((TimestampData) a).getSeconds() * 1000);
                // millis = HsqlDateTime.getNormalisedDate(cal, millis); 
                java.sql.Date value = new java.sql.Date(millis);
                return value;
            }
        case Types.SQL_TIMESTAMP:
            {
                Calendar cal = session.getCalendar();
                long millis = HsqlDateTime.convertMillisToCalendar(cal, ((TimestampData) a).getSeconds() * 1000);
                java.sql.Timestamp value = new java.sql.Timestamp(millis);
                value.setNanos(((TimestampData) a).getNanos());
                return value;
            }
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            {
                long seconds = ((TimestampData) a).getSeconds();
                java.sql.Timestamp value = new java.sql.Timestamp(seconds * 1000);
                value.setNanos(((TimestampData) a).getNanos());
                return value;
            }
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
    }
}
