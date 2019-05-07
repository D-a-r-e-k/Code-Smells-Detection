public Type[] getColumnTypes() {
    if (columnTypes.length == indexLimitVisible) {
        return columnTypes;
    }
    Type[] types = new Type[indexLimitVisible];
    ArrayUtil.copyArray(columnTypes, types, types.length);
    return types;
}
