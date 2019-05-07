public static ResultMetaData newResultMetaData(int colCount) {
    Type[] types = new Type[colCount];
    return newResultMetaData(types, null, colCount, colCount);
}
