public int compare(Session session, Object a, Object b) {
    long diff;
    if (a == b) {
        return 0;
    }
    if (a == null) {
        return -1;
    }
    if (b == null) {
        return 1;
    }
    switch(typeCode) {
        case Types.SQL_TIME:
        case Types.SQL_TIME_WITH_TIME_ZONE:
            {
                diff = ((TimeData) a).getSeconds() - ((TimeData) b).getSeconds();
                if (diff == 0) {
                    diff = ((TimeData) a).getNanos() - ((TimeData) b).getNanos();
                }
                return diff == 0 ? 0 : diff > 0 ? 1 : -1;
            }
        case Types.SQL_DATE:
        case Types.SQL_TIMESTAMP:
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            {
                diff = ((TimestampData) a).getSeconds() - ((TimestampData) b).getSeconds();
                if (diff == 0) {
                    diff = ((TimestampData) a).getNanos() - ((TimestampData) b).getNanos();
                }
                return diff == 0 ? 0 : diff > 0 ? 1 : -1;
            }
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
    }
}
