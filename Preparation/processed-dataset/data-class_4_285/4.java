public String getJDBCClassName() {
    switch(typeCode) {
        case Types.SQL_DATE:
            return "java.sql.Date";
        case Types.SQL_TIME:
        case Types.SQL_TIME_WITH_TIME_ZONE:
            return "java.sql.Time";
        case Types.SQL_TIMESTAMP:
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            return "java.sql.Timestamp";
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "DateTimeType");
    }
}
