/**
     * for compatibility, convert a datetime character string to a datetime
     * value for comparison
     */
private boolean convertDateTimeLiteral(Session session, Expression a, Expression b) {
    if (a.dataType.isDateTimeType()) {
    } else if (b.dataType.isDateTimeType()) {
        Expression c = a;
        a = b;
        b = c;
    } else {
        return false;
    }
    if (a.dataType.isDateTimeTypeWithZone()) {
        return false;
    }
    if (b.opType == OpTypes.VALUE && b.dataType.isCharacterType()) {
        b.valueData = a.dataType.castToType(session, b.valueData, b.dataType);
        b.dataType = a.dataType;
        return true;
    }
    return false;
}
