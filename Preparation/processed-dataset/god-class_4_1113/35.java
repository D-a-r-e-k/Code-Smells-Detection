public Object changeZone(Object a, Type otherType, int targetZone, int localZone) {
    if (a == null) {
        return null;
    }
    if (otherType.typeCode == Types.SQL_TIMESTAMP_WITH_TIME_ZONE || otherType.typeCode == Types.SQL_TIME_WITH_TIME_ZONE) {
        localZone = 0;
    }
    if (targetZone > DTIType.timezoneSecondsLimit || -targetZone > DTIType.timezoneSecondsLimit) {
        throw Error.error(ErrorCode.X_22009);
    }
    switch(typeCode) {
        case Types.SQL_TIME_WITH_TIME_ZONE:
            {
                TimeData value = (TimeData) a;
                if (localZone != 0 || value.zone != targetZone) {
                    return new TimeData(value.getSeconds() - localZone, value.getNanos(), targetZone);
                }
                break;
            }
        case Types.SQL_TIMESTAMP_WITH_TIME_ZONE:
            {
                TimestampData value = (TimestampData) a;
                if (localZone != 0 || value.zone != targetZone) {
                    return new TimestampData(value.getSeconds() - localZone, value.getNanos(), targetZone);
                }
                break;
            }
    }
    return a;
}
