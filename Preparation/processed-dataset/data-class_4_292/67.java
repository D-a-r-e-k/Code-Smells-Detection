String getValueClassName() {
    Type type = dataType == null ? NullType.getNullType() : dataType;
    return type.getJDBCClassName();
}
