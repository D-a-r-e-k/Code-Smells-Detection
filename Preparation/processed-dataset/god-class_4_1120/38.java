void setAsConstantValue(Session session) {
    valueData = getValue(session);
    opType = OpTypes.VALUE;
    nodes = emptyArray;
}
