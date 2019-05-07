public String getSqlType(Column column) {
    if (column.getSqlType() == null) {
        return "";
    }
    String sqlType = column.getSqlType().toUpperCase();
    int scale = column.getScale();
    int precision = column.getPrecision();
    int length = column.getLength();
    if (sqlType == null)
        return "";
    if (sqlType.equals("NUMBER") || sqlType.equals("DOUBLE") || contains(sqlType, "INT") || sqlType.equals("YEAR") || sqlType.equals("FLOAT") || sqlType.equals("DECIMAL")) {
        if (precision > 0) {
            sqlType = sqlType + "(" + precision;
            if (scale != 0) {
                sqlType = sqlType + ", " + scale;
            }
            sqlType = sqlType + ")";
        }
        return sqlType;
    }
    if (sqlType.indexOf("CHAR") > -1) {
        if (length > 0) {
            return sqlType + "(" + length + ")";
        }
    }
    return sqlType;
}
