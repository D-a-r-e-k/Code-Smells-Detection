public Object subtract(Object a, Object b, Type otherType) {
    if (a == null || b == null) {
        return null;
    }
    switch(typeCode) {
        case Types.SQL_TIME_WITH_TIME_ZONE:
        case Types.SQL_TIME:
            if (b instanceof IntervalMonthData) {
                throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
            } else if (b instanceof IntervalSecondData) {
                return addSeconds((TimeData) a, -(int) ((IntervalSecondData) b).units, -((IntervalSecondData) b).nanos);
            }
            break;
        case Types.SQL_DATE:
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
        case Types.SQL_TIMESTAMP:
            if (b instanceof IntervalMonthData) {
                return addMonths((TimestampData) a, -(int) ((IntervalMonthData) b).units);
            } else if (b instanceof IntervalSecondData) {
                return addSeconds((TimestampData) a, -(int) ((IntervalSecondData) b).units, -((IntervalSecondData) b).nanos);
            }
            break;
        default:
    }
    throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
}
