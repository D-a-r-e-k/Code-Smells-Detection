/**
     * Helper method to determine the max size of a field.
     * -1 will be returned fields without a size specification.
     *
     * @return max size of a field.
     */
public int getMaxSize() {
    String jdbcType = getJdbcType().toString();
    int size;
    if ("VARCHAR".equals(jdbcType) || "CHAR".equals(jdbcType) || "LONGVARCHAR".equals(jdbcType)) {
        size = -1;
    } else {
        return -1;
    }
    String sqlType = getSqlType().toString();
    if (sqlType == null) {
        return size;
    }
    int beginIndex = sqlType.indexOf("(");
    int endIndex = sqlType.indexOf(",");
    if (endIndex == -1) {
        endIndex = sqlType.indexOf(")");
    }
    if (beginIndex == -1 || endIndex == -1) {
        return size;
    }
    String strSize = sqlType.substring(beginIndex + 1, endIndex);
    try {
        size = Integer.parseInt(strSize);
        return size;
    } catch (Exception e) {
        return size;
    }
}
