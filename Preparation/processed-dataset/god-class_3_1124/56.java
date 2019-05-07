public HsqlName[] getTableNamesForWrite() {
    if (statement == null) {
        return HsqlName.emptyArray;
    }
    return statement.getTableNamesForWrite();
}
