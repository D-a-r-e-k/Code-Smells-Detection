public Object convertToType(SessionInterface session, Object a, Type otherType) {
    if (a == null) {
        return a;
    }
    switch(otherType.typeCode) {
        case Types.SQL_CLOB:
            a = a.toString();
        //fall through 
        case Types.SQL_CHAR:
        case Types.SQL_VARCHAR:
        case Types.VARCHAR_IGNORECASE:
            switch(this.typeCode) {
                case Types.SQL_DATE:
                case Types.SQL_TIME_WITH_TIME_ZONE:
                case Types.SQL_TIME:
                case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
                case Types.SQL_TIMESTAMP:
                    {
                        return session.getScanner().convertToDatetimeInterval(session, (String) a, this);
                    }
            }
            break;
        case Types.SQL_DATE:
        case Types.SQL_TIME:
        case Types.SQL_TIME_WITH_TIME_ZONE:
        case Types.SQL_TIMESTAMP:
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            break;
        default:
            throw Error.error(ErrorCode.X_42561);
    }
    switch(this.typeCode) {
        case Types.SQL_DATE:
            switch(otherType.typeCode) {
                case Types.SQL_DATE:
                    return a;
                case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
                    {
                        long seconds = ((TimestampData) a).getSeconds() + ((TimestampData) a).getZone();
                        long l = HsqlDateTime.getNormalisedDate(seconds * 1000);
                        return new TimestampData(l / 1000);
                    }
                case Types.SQL_TIMESTAMP:
                    {
                        long l = HsqlDateTime.getNormalisedDate(((TimestampData) a).getSeconds() * 1000);
                        return new TimestampData(l / 1000);
                    }
                default:
                    throw Error.error(ErrorCode.X_42561);
            }
        case Types.SQL_TIME_WITH_TIME_ZONE:
            switch(otherType.typeCode) {
                case Types.SQL_TIME_WITH_TIME_ZONE:
                    return convertToTypeLimits(session, a);
                case Types.SQL_TIME:
                    {
                        TimeData ti = (TimeData) a;
                        return new TimeData(ti.getSeconds() - session.getZoneSeconds(), scaleNanos(ti.getNanos()), session.getZoneSeconds());
                    }
                case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
                    {
                        TimestampData ts = (TimestampData) a;
                        long seconds = HsqlDateTime.convertToNormalisedTime(ts.getSeconds() * 1000) / 1000;
                        return new TimeData((int) (seconds), scaleNanos(ts.getNanos()), ts.getZone());
                    }
                case Types.SQL_TIMESTAMP:
                    {
                        TimestampData ts = (TimestampData) a;
                        long seconds = ts.getSeconds() - session.getZoneSeconds();
                        seconds = HsqlDateTime.convertToNormalisedTime(seconds * 1000) / 1000;
                        return new TimeData((int) (seconds), scaleNanos(ts.getNanos()), session.getZoneSeconds());
                    }
                default:
                    throw Error.error(ErrorCode.X_42561);
            }
        case Types.SQL_TIME:
            switch(otherType.typeCode) {
                case Types.SQL_TIME_WITH_TIME_ZONE:
                    {
                        TimeData ti = (TimeData) a;
                        return new TimeData(ti.getSeconds() + ti.getZone(), scaleNanos(ti.getNanos()), 0);
                    }
                case Types.SQL_TIME:
                    return convertToTypeLimits(session, a);
                case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
                    {
                        TimestampData ts = (TimestampData) a;
                        long seconds = ts.getSeconds() + ts.getZone();
                        seconds = HsqlDateTime.convertToNormalisedTime(seconds * 1000) / 1000;
                        return new TimeData((int) (seconds), scaleNanos(ts.getNanos()), 0);
                    }
                case Types.SQL_TIMESTAMP:
                    TimestampData ts = (TimestampData) a;
                    long seconds = HsqlDateTime.convertToNormalisedTime(ts.getSeconds() * 1000) / 1000;
                    return new TimeData((int) (seconds), scaleNanos(ts.getNanos()));
                default:
                    throw Error.error(ErrorCode.X_42561);
            }
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            switch(otherType.typeCode) {
                case Types.SQL_TIME_WITH_TIME_ZONE:
                    {
                        TimeData ti = (TimeData) a;
                        long seconds = session.getCurrentDate().getSeconds() + ti.getSeconds();
                        return new TimestampData(seconds, scaleNanos(ti.getNanos()), ti.getZone());
                    }
                case Types.SQL_TIME:
                    {
                        TimeData ti = (TimeData) a;
                        long seconds = session.getCurrentDate().getSeconds() + ti.getSeconds() - session.getZoneSeconds();
                        return new TimestampData(seconds, scaleNanos(ti.getNanos()), session.getZoneSeconds());
                    }
                case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
                    return convertToTypeLimits(session, a);
                case Types.SQL_TIMESTAMP:
                    {
                        TimestampData ts = (TimestampData) a;
                        long seconds = ts.getSeconds() - session.getZoneSeconds();
                        return new TimestampData(seconds, scaleNanos(ts.getNanos()), session.getZoneSeconds());
                    }
                case Types.SQL_DATE:
                    {
                        TimestampData ts = (TimestampData) a;
                        return new TimestampData(ts.getSeconds(), 0, session.getZoneSeconds());
                    }
                default:
                    throw Error.error(ErrorCode.X_42561);
            }
        case Types.SQL_TIMESTAMP:
            switch(otherType.typeCode) {
                case Types.SQL_TIME_WITH_TIME_ZONE:
                    {
                        TimeData ti = (TimeData) a;
                        long seconds = session.getCurrentDate().getSeconds() + ti.getSeconds() - session.getZoneSeconds();
                        return new TimestampData(seconds, scaleNanos(ti.getNanos()), session.getZoneSeconds());
                    }
                case Types.SQL_TIME:
                    {
                        TimeData ti = (TimeData) a;
                        long seconds = session.getCurrentDate().getSeconds() + ti.getSeconds();
                        return new TimestampData(seconds, scaleNanos(ti.getNanos()));
                    }
                case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
                    {
                        TimestampData ts = (TimestampData) a;
                        long seconds = ts.getSeconds() + ts.getZone();
                        return new TimestampData(seconds, scaleNanos(ts.getNanos()));
                    }
                case Types.SQL_TIMESTAMP:
                    return convertToTypeLimits(session, a);
                case Types.SQL_DATE:
                    return a;
                default:
                    throw Error.error(ErrorCode.X_42561);
            }
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
    }
}
