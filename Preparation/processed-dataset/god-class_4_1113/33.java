public DateTimeType getDateTimeTypeWithoutZone() {
    if (this.withTimeZone) {
        switch(typeCode) {
            case Types.SQL_TIME_WITH_TIME_ZONE:
                if (scale != DTIType.defaultTimeFractionPrecision) {
                    return new DateTimeType(Types.SQL_TIME, Types.SQL_TIME, scale);
                }
                return SQL_TIME;
            case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
                if (scale != DTIType.defaultTimestampFractionPrecision) {
                    return new DateTimeType(Types.SQL_TIMESTAMP, Types.SQL_TIMESTAMP, scale);
                }
                return SQL_TIMESTAMP;
            default:
                throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
        }
    }
    return this;
}
