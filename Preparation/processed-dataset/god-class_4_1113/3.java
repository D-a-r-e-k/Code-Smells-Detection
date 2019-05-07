public Class getJDBCClass() {
    switch(typeCode) {
        case Types.SQL_DATE:
            return java.sql.Date.class;
        case Types.SQL_TIME:
        case Types.SQL_TIME_WITH_TIME_ZONE:
            return java.sql.Time.class;
        case Types.SQL_TIMESTAMP:
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            return java.sql.Timestamp.class;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
    }
}
