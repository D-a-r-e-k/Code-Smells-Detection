/**
     * Returns a new function Routine object based solely on a Java Method object.
     */
public static Routine newRoutine(Session session, Method method) {
    Routine routine = new Routine(SchemaObject.FUNCTION);
    int offset = 0;
    Class[] params = method.getParameterTypes();
    String className = method.getDeclaringClass().getName();
    StringBuffer sb = new StringBuffer();
    sb.append("CLASSPATH:");
    sb.append(method.getDeclaringClass().getName()).append('.');
    sb.append(method.getName());
    if (params.length > 0 && params[0].equals(java.sql.Connection.class)) {
        offset = 1;
    }
    String name = sb.toString();
    if (className.equals("java.lang.Math")) {
        routine.isLibraryRoutine = true;
    }
    for (int j = offset; j < params.length; j++) {
        Type methodParamType = Types.getParameterSQLType(params[j]);
        ColumnSchema param = new ColumnSchema(null, methodParamType, !params[j].isPrimitive(), false, null);
        routine.addParameter(param);
    }
    routine.setLanguage(Routine.LANGUAGE_JAVA);
    routine.setMethod(method);
    routine.setMethodURL(name);
    routine.setDataImpact(Routine.NO_SQL);
    Type methodReturnType = Types.getParameterSQLType(method.getReturnType());
    routine.javaMethodWithConnection = offset == 1;
    ;
    routine.setReturnType(methodReturnType);
    routine.resolve(session);
    return routine;
}
