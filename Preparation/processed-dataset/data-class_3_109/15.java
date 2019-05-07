public String getJdbcType(Column column) {
    if (column.getSqlType() == null) {
        return "";
    }
    String sqlType = column.getSqlType().toUpperCase();
    int scale = column.getScale();
    int precision = column.getPrecision();
    if (sqlType == null)
        return "";
    if (sqlType.equals("NUMERIC") || sqlType.equals("TINYINT") || sqlType.equals("SMALLINT") || sqlType.equals("DOUBLE") || sqlType.equals("TIMESTAMP") || sqlType.equals("FLOAT") || sqlType.equals("DATE") || sqlType.equals("TIME") || contains(sqlType, "BIGINT") || sqlType.equals("DECIMAL"))
        return sqlType;
    if (contains(sqlType, "CHAR")) {
        return "VARCHAR";
    }
    if (contains(sqlType, "TEXT")) {
        return "VARCHAR";
    }
    if (sqlType.equals("DATETIME"))
        return "TIMESTAMP";
    // Postgress specific:  
    if (sqlType.equals("FLOAT(7)"))
        return "FLOAT";
    // Postgress specific:  
    if (sqlType.equals("FLOAT8"))
        return "DOUBLE";
    if (sqlType.equals("BYTEA"))
        return "VARBINARY";
    if (contains(sqlType, "BLOB")) {
        return "BLOB";
    }
    if (contains(sqlType, "SERIAL"))
        return "INTEGER";
    if (contains(sqlType, "IDENTITY"))
        return "INTEGER";
    if (sqlType.equals("NUMBER") || contains(sqlType, "INT") || sqlType.equals("YEAR")) {
        if (scale == 0) {
            if (precision == 0) {
                // this is the case for pseudo columns  
                // like sequences, count(*) etc.  
                // by convention, let's convert them to Integer  
                return "INTEGER";
            }
            if (precision <= 5) {
                //let it be a byte  
                return "INTEGER";
            }
            if (precision <= 9) {
                //let it be an int  
                return "INTEGER";
            }
            if (precision <= 18) {
                if (sqlType.indexOf("INT") != -1) {
                    return "INTEGER";
                }
                //let it be a long  
                return "BIGINT";
            }
        }
        if (precision + scale <= 12) {
            return "DECIMAL";
        }
        if (precision + scale <= 64) {
            return "DOUBLE";
        }
    }
    return "JAVA_OBJECT";
}
