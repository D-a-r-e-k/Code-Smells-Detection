public String getType(Column column) {
    if (column.getSqlType() == null) {
        return "";
    }
    String sqlType = column.getSqlType().toUpperCase();
    int scale = column.getScale();
    int precision = column.getPrecision();
    if (sqlType == null)
        return "";
    if (sqlType.equals("DATE"))
        return "java.sql.Date";
    if (sqlType.equals("BOOL"))
        return "java.lang.Boolean";
    if (sqlType.equals("FLOAT"))
        return "java.lang.Float";
    if (sqlType.equals("DOUBLE"))
        return "java.lang.Double";
    if (sqlType.equals("FLOAT(7)"))
        return "java.lang.Float";
    if (sqlType.equals("FLOAT8"))
        return "java.lang.Double";
    if (contains(sqlType, "NUMERIC") || contains(sqlType, "NUMERIC"))
        return "java.math.BigDecimal";
    if (sqlType.equals("BYTEA")) {
        System.out.println("Mapping the BYTEA binary type to java.sql.Blob. JAG has no support for binary fields.");
        return "java.sql.Blob";
    }
    if ((sqlType.indexOf("TIMESTAMP") != -1) || (sqlType.indexOf("DATETIME") != -1))
        return "java.sql.Timestamp";
    if (sqlType.equals("TIME"))
        return "java.sql.Time";
    if (contains(sqlType, "TINYINT"))
        return "java.lang.Byte";
    if (contains(sqlType, "SMALLINT"))
        return "java.lang.Short";
    if (contains(sqlType, "BIGINT"))
        return "java.lang.Long";
    if (contains(sqlType, "DECIMAL"))
        return "java.math.BigDecimal";
    if (contains(sqlType, "BLOB"))
        return "java.sql.Blob";
    if (contains(sqlType, "SERIAL"))
        return "java.lang.Long";
    if (contains(sqlType, "IDENTITY"))
        return "java.lang.Long";
    if (sqlType.equals("NUMBER") || sqlType.equals("INT") || sqlType.equals("YEAR") || sqlType.indexOf("INT") > -1) {
        if (scale == 0) {
            if (precision == 0) {
                // this is the case for pseudo columns  
                // like sequences, count(*) etc.  
                // by convention, let's convert them to Integer  
                return "java.lang.Integer";
            }
            if (precision <= 2) {
                //let it be a byte  
                //  
                return "java.lang.Integer";
            }
            if (precision <= 5) {
                return "java.lang.Integer";
            }
            if (precision <= 9) {
                //let it be an int  
                return "java.lang.Integer";
            }
            if (precision <= 18) {
                if (sqlType.indexOf("INT") != -1) {
                    return "java.lang.Integer";
                }
                //let it be a long  
                return "java.lang.Long";
            } else {
                return "java.math.BigDecimal";
            }
        }
        if (precision + scale <= 12) {
            //return "java.lang.Float";  
            return "java.math.BigDecimal";
        }
        if (precision + scale <= 64) {
            return "java.lang.Double";
        } else {
            return "java.math.BigDecimal";
        }
    }
    if (sqlType.indexOf("CHAR") > -1) {
        return "java.lang.String";
    }
    if (sqlType.indexOf("TEXT") > -1) {
        return "java.lang.String";
    }
    System.out.println("unknown sql type: " + sqlType + " Map it to a String.");
    return "java.lang.String";
}
