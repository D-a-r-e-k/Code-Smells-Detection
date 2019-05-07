/**
     * Set the data type
     */
void setDataType(Session session, Type type) {
    if (opType == OpTypes.VALUE) {
        valueData = type.convertToType(session, valueData, dataType);
    }
    dataType = type;
}
