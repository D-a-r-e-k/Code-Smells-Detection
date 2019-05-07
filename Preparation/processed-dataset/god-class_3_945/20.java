/**
    * Determine if the field can be used with a sequence.
    * For this the following must apply:
    * - the field needs to be a primary key
    * - the autogenerate primary key checkbox should be on.
    * - the database should be oracle or postgresql with a Serial field.
    *
    * @return true if sequence is allowed
    */
public boolean isSequenceField() {
    if (isPrimaryKey() && getHasAutoGenPrimaryKey()) {
        Datasource d = getDatasource();
        String mapping = d.getTypeMapping().getLower();
        if (mapping.startsWith("mysql")) {
            return false;
        }
        // For Oracle there is no autoincrement field. Always use true.  
        if (mapping.startsWith("oracle")) {
            return true;
        }
        if (mapping.startsWith("postgresql")) {
            // Postgresql supporst sequence, but if the field is a Serial field, we don't want to use them.  
            if (getSqlType().getLower().startsWith("serial")) {
                return false;
            } else {
                return true;
            }
        }
        if (mapping.startsWith("hypersonic")) {
            if (getSqlType().getLower().startsWith("identity")) {
                return false;
            } else {
                return true;
            }
        }
    }
    // By default, return false.  
    return false;
}
