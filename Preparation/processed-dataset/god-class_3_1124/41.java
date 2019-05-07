public void resolve(Session session) {
    setLanguage(language);
    if (language == Routine.LANGUAGE_SQL) {
        if (dataImpact == NO_SQL) {
            throw Error.error(ErrorCode.X_42604);
        }
        if (parameterStyle == PARAM_STYLE_JAVA) {
            throw Error.error(ErrorCode.X_42604);
        }
    }
    if (language == Routine.LANGUAGE_SQL) {
        if (parameterStyle != 0 && parameterStyle != PARAM_STYLE_SQL) {
            throw Error.error(ErrorCode.X_42604);
        }
    }
    parameterTypes = new Type[parameterList.size()];
    typeGroups = 0;
    for (int i = 0; i < parameterTypes.length; i++) {
        ColumnSchema param = (ColumnSchema) parameterList.get(i);
        parameterTypes[i] = param.dataType;
        if (i < 4) {
            BitMap.setByte(typeGroups, (byte) param.dataType.typeComparisonGroup, i * 8);
        }
    }
    if (statement != null) {
        statement.resolve(session);
        if (dataImpact == CONTAINS_SQL) {
            checkNoSQLData(session.database, statement.getReferences());
        }
    }
    if (methodName != null && javaMethod == null) {
        boolean[] hasConnection = new boolean[1];
        javaMethod = getMethod(methodName, this, hasConnection, returnsTable);
        if (javaMethod == null) {
            throw Error.error(ErrorCode.X_46103);
        }
        javaMethodWithConnection = hasConnection[0];
        String className = javaMethod.getDeclaringClass().getName();
        if (className.equals("java.lang.Math")) {
            isLibraryRoutine = true;
        }
    }
    if (isAggregate) {
        if (parameterTypes.length != 4) {
            throw Error.error(ErrorCode.X_42610);
        }
        boolean check = parameterTypes[1].typeCode == Types.BOOLEAN;
        // 
        ColumnSchema param = (ColumnSchema) parameterList.get(0);
        check &= param.getParameterMode() == SchemaObject.ParameterModes.PARAM_IN;
        param = (ColumnSchema) parameterList.get(1);
        check &= param.getParameterMode() == SchemaObject.ParameterModes.PARAM_IN;
        param = (ColumnSchema) parameterList.get(2);
        check &= param.getParameterMode() == SchemaObject.ParameterModes.PARAM_INOUT;
        param = (ColumnSchema) parameterList.get(3);
        check &= param.getParameterMode() == SchemaObject.ParameterModes.PARAM_INOUT;
        if (!check) {
            throw Error.error(ErrorCode.X_42610);
        }
    }
    setReferences();
}
