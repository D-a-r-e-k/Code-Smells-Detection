public Type getCombinedType(Type other, int operation) {
    switch(operation) {
        case OpTypes.EQUAL:
        case OpTypes.GREATER:
        case OpTypes.GREATER_EQUAL:
        case OpTypes.SMALLER_EQUAL:
        case OpTypes.SMALLER:
        case OpTypes.NOT_EQUAL:
            {
                if (typeCode == other.typeCode) {
                    return this;
                }
                if (other.typeCode == Types.SQL_ALL_TYPES) {
                    return this;
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
        case OpTypes.ADD:
        case OpTypes.SUBTRACT:
            if (other.isIntervalType()) {
                if (typeCode != Types.SQL_DATE && other.scale > scale) {
                    return getDateTimeType(typeCode, other.scale);
                }
                return this;
            }
            break;
        default:
    }
    throw Error.error(ErrorCode.X_42562);
}
