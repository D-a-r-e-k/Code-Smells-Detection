void convertArgsToSQL(Session session, Object[] callArguments, Object[] data) {
    int extraArg = javaMethodWithConnection ? 1 : 0;
    Type[] types = getParameterTypes();
    for (int i = 0; i < callArguments.length; i++) {
        Object value = data[i + extraArg];
        ColumnSchema param = getParameter(i);
        if (param.parameterMode != SchemaObject.ParameterModes.PARAM_IN) {
            value = java.lang.reflect.Array.get(value, 0);
        }
        callArguments[i] = types[i].convertJavaToSQL(session, value);
    }
}
