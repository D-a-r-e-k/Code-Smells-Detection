static Method getMethod(String name, Routine routine, boolean[] hasConnection, boolean returnsTable) {
    int i = name.indexOf(':');
    if (i != -1) {
        if (!name.substring(0, i).equals(SqlInvariants.CLASSPATH_NAME)) {
            throw Error.error(ErrorCode.X_46102, name);
        }
        name = name.substring(i + 1);
    }
    Method[] methods = getMethods(name);
    int firstMismatch = -1;
    for (i = 0; i < methods.length; i++) {
        int offset = 0;
        hasConnection[0] = false;
        Method method = methods[i];
        Class[] params = method.getParameterTypes();
        if (params.length > 0 && params[0].equals(java.sql.Connection.class)) {
            offset = 1;
            hasConnection[0] = true;
        }
        if (params.length - offset != routine.parameterTypes.length) {
            continue;
        }
        if (returnsTable) {
            if (!java.sql.ResultSet.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }
        } else {
            Type methodReturnType = Types.getParameterSQLType(method.getReturnType());
            if (methodReturnType == null) {
                continue;
            }
            if (methodReturnType.typeCode != routine.returnType.typeCode) {
                continue;
            }
        }
        for (int j = 0; j < routine.parameterTypes.length; j++) {
            boolean isInOut = false;
            Class param = params[j + offset];
            if (param.isArray()) {
                if (!byte[].class.equals(param)) {
                    param = param.getComponentType();
                    if (param.isPrimitive()) {
                        method = null;
                        break;
                    }
                    isInOut = true;
                }
            }
            Type methodParamType = Types.getParameterSQLType(param);
            if (methodParamType == null) {
                method = null;
                break;
            }
            boolean result = routine.parameterTypes[j].typeComparisonGroup == methodParamType.typeComparisonGroup;
            // exact type for number 
            if (result && routine.parameterTypes[j].isNumberType()) {
                result = routine.parameterTypes[j].typeCode == methodParamType.typeCode;
            }
            if (isInOut && routine.getParameter(j).parameterMode == SchemaObject.ParameterModes.PARAM_IN) {
                result = false;
            }
            if (!result) {
                method = null;
                if (j + offset > firstMismatch) {
                    firstMismatch = j + offset;
                }
                break;
            }
        }
        if (method != null) {
            for (int j = 0; j < routine.parameterTypes.length; j++) {
                routine.getParameter(j).setNullable(!params[j + offset].isPrimitive());
            }
            return method;
        }
    }
    if (firstMismatch >= 0) {
        ColumnSchema param = routine.getParameter(firstMismatch);
        throw Error.error(ErrorCode.X_46511, param.getNameString());
    }
    return null;
}
