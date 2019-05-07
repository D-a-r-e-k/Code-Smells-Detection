public HsqlName[] getTableNamesForRead() {
    if (statement == null) {
        return HsqlName.emptyArray;
    }
    return statement.getTableNamesForRead();
}
