Object[] convertArgsToJava(Session session, Object[] callArguments) {
    int extraArg = javaMethodWithConnection ? 1 : 0;
    Object[] data = new Object[callArguments.length + extraArg];
    Type[] types = getParameterTypes();
    for (int i = 0; i < callArguments.length; i++) {
        Object value = callArguments[i];
        ColumnSchema param = getParameter(i);
        if (param.parameterMode == SchemaObject.ParameterModes.PARAM_IN) {
            data[i + extraArg] = types[i].convertSQLToJava(session, value);
        } else {
            Object jdbcValue = types[i].convertSQLToJava(session, value);
            Class cl = types[i].getJDBCClass();
            Object array = java.lang.reflect.Array.newInstance(cl, 1);
            java.lang.reflect.Array.set(array, 0, jdbcValue);
            data[i + extraArg] = array;
        }
    }
    return data;
}
