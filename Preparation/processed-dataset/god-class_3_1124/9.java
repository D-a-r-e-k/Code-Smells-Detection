public String getSQL() {
    StringBuffer sb = new StringBuffer();
    sb.append(Tokens.T_CREATE).append(' ');
    if (isAggregate) {
        sb.append(Tokens.T_AGGREGATE).append(' ');
    }
    if (routineType == SchemaObject.PROCEDURE) {
        sb.append(Tokens.T_PROCEDURE);
    } else {
        sb.append(Tokens.T_FUNCTION);
    }
    sb.append(' ');
    sb.append(name.getSchemaQualifiedStatementName());
    sb.append('(');
    for (int i = 0; i < parameterList.size(); i++) {
        if (i > 0) {
            sb.append(',');
        }
        ColumnSchema param = (ColumnSchema) parameterList.get(i);
        // in - out 
        sb.append(param.getSQL());
    }
    sb.append(')');
    sb.append(' ');
    if (routineType == SchemaObject.FUNCTION) {
        sb.append(Tokens.T_RETURNS);
        sb.append(' ');
        if (returnsTable) {
            sb.append(Tokens.T_TABLE);
            sb.append(returnTable.getColumnListWithTypeSQL());
        } else {
            sb.append(returnType.getTypeDefinition());
        }
        sb.append(' ');
    }
    // SPECIFIC 
    if (specificName != null) {
        sb.append(Tokens.T_SPECIFIC);
        sb.append(' ');
        sb.append(specificName.getStatementName());
        sb.append(' ');
    }
    // 
    sb.append(Tokens.T_LANGUAGE);
    sb.append(' ');
    if (language == LANGUAGE_JAVA) {
        sb.append(Tokens.T_JAVA);
    } else {
        sb.append(Tokens.T_SQL);
    }
    sb.append(' ');
    // 
    if (!isDeterministic) {
        sb.append(Tokens.T_NOT);
        sb.append(' ');
    }
    sb.append(Tokens.T_DETERMINISTIC);
    sb.append(' ');
    // 
    sb.append(getDataImpactString());
    sb.append(' ');
    // 
    if (routineType == SchemaObject.FUNCTION) {
        if (isNullInputOutput) {
            sb.append(Tokens.T_RETURNS).append(' ').append(Tokens.T_NULL);
        } else {
            sb.append(Tokens.T_CALLED);
        }
        sb.append(' ').append(Tokens.T_ON).append(' ');
        sb.append(Tokens.T_NULL).append(' ').append(Tokens.T_INPUT);
        sb.append(' ');
    } else {
        if (isNewSavepointLevel) {
            sb.append(Tokens.T_NEW);
        } else {
            sb.append(Tokens.T_OLD);
        }
        sb.append(' ').append(Tokens.T_SAVEPOINT).append(' ');
        sb.append(Tokens.T_LEVEL).append(' ');
    }
    if (language == LANGUAGE_JAVA) {
        sb.append(Tokens.T_EXTERNAL).append(' ').append(Tokens.T_NAME);
        sb.append(' ').append('\'').append(methodName).append('\'');
    } else {
        sb.append(statement.getSQL());
    }
    return sb.toString();
}
