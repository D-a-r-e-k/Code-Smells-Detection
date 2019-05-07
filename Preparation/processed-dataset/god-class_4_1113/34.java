public static DateTimeType getDateTimeType(int type, int scale) {
    if (scale > DTIType.maxFractionPrecision) {
        throw Error.error(ErrorCode.X_42592);
    }
    switch(type) {
        case Types.SQL_DATE:
            return SQL_DATE;
        case Types.SQL_TIME:
            if (scale != DTIType.defaultTimeFractionPrecision) {
                return new DateTimeType(Types.SQL_TIME, type, scale);
            }
            return SQL_TIME;
        case Types.SQL_TIME_WITH_TIME_ZONE:
            if (scale != DTIType.defaultTimeFractionPrecision) {
                return new DateTimeType(Types.SQL_TIME, type, scale);
            }
            return SQL_TIME_WITH_TIME_ZONE;
        case Types.SQL_TIMESTAMP:
            if (scale != DTIType.defaultTimestampFractionPrecision) {
                return new DateTimeType(Types.SQL_TIMESTAMP, type, scale);
            }
            return SQL_TIMESTAMP;
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            if (scale != DTIType.defaultTimestampFractionPrecision) {
                return new DateTimeType(Types.SQL_TIMESTAMP, type, scale);
            }
            return SQL_TIMESTAMP_WITH_TIME_ZONE;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
    }
}
