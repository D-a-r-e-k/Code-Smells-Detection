/** yet another mapping...
    *
    * These mappings are as far as possible database-generic, using Oracle specifics where genericity (is that a word?)
    * is not possible (Oracle-specific types are automatically re-typed in JAG if the user selects a non-Oracle database
    * in the 'Datasource' settings).
    * NOTE: 'Genericity' is more important than accuracy here..
    *
    * @return a two-length String array [sql type, jdbc type]
    */
private String[] getDatabaseColumnTypesForClass(String javaClass) {
    if (Byte.class.getName().equals(javaClass) || Short.class.getName().equals(javaClass) || Integer.class.getName().equals(javaClass) || Long.class.getName().equals(javaClass) || Double.class.getName().equals(javaClass)) {
        return new String[] { NUMBER_SQL_TYPE, BIGINT_JDBC_TYPE };
    }
    if (java.sql.Timestamp.class.getName().equals(javaClass) || java.sql.Date.class.getName().equals(javaClass) || java.util.Date.class.getName().equals(javaClass)) {
        return new String[] { TIMESTAMP_SQL_TYPE, TIMESTAMP_SQL_TYPE };
    }
    return new String[] { DEFAULT_SQL_TYPE, DEFAULT_JDBC_TYPE };
}
