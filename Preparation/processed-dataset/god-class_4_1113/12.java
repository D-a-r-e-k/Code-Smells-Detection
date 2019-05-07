public Type getAggregateType(Type other) {
    // DATE with DATE returned here 
    if (typeCode == other.typeCode) {
        return scale >= other.scale ? this : other;
    }
    if (other.typeCode == Types.SQL_ALL_TYPES) {
        return this;
    }
    if (other.isCharacterType()) {
        return other.getAggregateType(this);
    }
    if (!other.isDateTimeType()) {
        throw Error.error(ErrorCode.X_42562);
    }
    DateTimeType otherType = (DateTimeType) other;
    // DATE with TIME caught here 
    if (otherType.startIntervalType > endIntervalType || startIntervalType > otherType.endIntervalType) {
        throw Error.error(ErrorCode.X_42562);
    }
    int newType = typeCode;
    int scale = this.scale > otherType.scale ? this.scale : otherType.scale;
    boolean zone = withTimeZone || otherType.withTimeZone;
    int startType = otherType.startIntervalType > startIntervalType ? startIntervalType : otherType.startIntervalType;
    if (startType == Types.SQL_INTERVAL_HOUR) {
        newType = zone ? Types.SQL_TIME_WITH_TIME_ZONE : Types.SQL_TIME;
    } else {
        newType = zone ? Types.SQL_TIMESTAMP_WITH_TIME_ZONE : Types.SQL_TIMESTAMP;
    }
    return getDateTimeType(newType, scale);
}
