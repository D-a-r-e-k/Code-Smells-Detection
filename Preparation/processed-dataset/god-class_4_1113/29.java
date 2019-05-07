public int getPart(Session session, Object dateTime, int part) {
    int calendarPart;
    int increment = 0;
    int divisor = 1;
    switch(part) {
        case Types.SQL_INTERVAL_YEAR:
            calendarPart = Calendar.YEAR;
            break;
        case Types.SQL_INTERVAL_MONTH:
            increment = 1;
            calendarPart = Calendar.MONTH;
            break;
        case Types.SQL_INTERVAL_DAY:
        case DAY_OF_MONTH:
            calendarPart = Calendar.DAY_OF_MONTH;
            break;
        case Types.SQL_INTERVAL_HOUR:
            calendarPart = Calendar.HOUR_OF_DAY;
            break;
        case Types.SQL_INTERVAL_MINUTE:
            calendarPart = Calendar.MINUTE;
            break;
        case Types.SQL_INTERVAL_SECOND:
            calendarPart = Calendar.SECOND;
            break;
        case DAY_OF_WEEK:
            calendarPart = Calendar.DAY_OF_WEEK;
            break;
        case WEEK_OF_YEAR:
            calendarPart = Calendar.WEEK_OF_YEAR;
            break;
        case SECONDS_MIDNIGHT:
            {
                if (typeCode == Types.SQL_TIME || typeCode == Types.SQL_TIME_WITH_TIME_ZONE) {
                } else {
                    try {
                        Type target = withTimeZone ? Type.SQL_TIME_WITH_TIME_ZONE : Type.SQL_TIME;
                        dateTime = target.castToType(session, dateTime, this);
                    } catch (HsqlException e) {
                    }
                }
                return ((TimeData) dateTime).getSeconds();
            }
        case TIMEZONE_HOUR:
            if (typeCode == Types.SQL_TIMESTAMP_WITH_TIME_ZONE) {
                return ((TimestampData) dateTime).getZone() / 3600;
            } else {
                return ((TimeData) dateTime).getZone() / 3600;
            }
        case TIMEZONE_MINUTE:
            if (typeCode == Types.SQL_TIMESTAMP_WITH_TIME_ZONE) {
                return ((TimestampData) dateTime).getZone() / 60 % 60;
            } else {
                return ((TimeData) dateTime).getZone() / 60 % 60;
            }
        case QUARTER:
            increment = 1;
            divisor = 3;
            calendarPart = Calendar.MONTH;
            break;
        case DAY_OF_YEAR:
            calendarPart = Calendar.DAY_OF_YEAR;
            break;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType - " + part);
    }
    long millis;
    if (typeCode == Types.SQL_TIME || typeCode == Types.SQL_TIME_WITH_TIME_ZONE) {
        millis = (((TimeData) dateTime).getSeconds() + ((TimeData) dateTime).getZone()) * 1000;
    } else {
        millis = (((TimestampData) dateTime).getSeconds() + ((TimestampData) dateTime).getZone()) * 1000;
    }
    return HsqlDateTime.getDateTimePart(millis, calendarPart) / divisor + increment;
}
