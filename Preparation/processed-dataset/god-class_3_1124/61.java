static Method[] getMethods(String name) {
    int i = name.lastIndexOf('.');
    if (i == -1) {
        throw Error.error(ErrorCode.X_42501, name);
    }
    String className = name.substring(0, i);
    String methodname = name.substring(i + 1);
    Class cl;
    Method[] methods = null;
    try {
        cl = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
    } catch (Throwable t1) {
        try {
            cl = Class.forName(className);
        } catch (Throwable t) {
            throw Error.error(t, ErrorCode.X_42501, ErrorCode.M_Message_Pair, new Object[] { t.getMessage(), className });
        }
    }
    try {
        methods = cl.getMethods();
    } catch (Throwable t) {
        throw Error.error(t, ErrorCode.X_42501, ErrorCode.M_Message_Pair, new Object[] { t.getMessage(), className });
    }
    HsqlArrayList list = new HsqlArrayList();
    for (i = 0; i < methods.length; i++) {
        int offset = 0;
        Method method = methods[i];
        int modifiers = method.getModifiers();
        if (!method.getName().equals(methodname) || !Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
            continue;
        }
        Class[] params = methods[i].getParameterTypes();
        if (params.length > 0 && params[0].equals(java.sql.Connection.class)) {
            offset = 1;
        }
        for (int j = offset; j < params.length; j++) {
            Class param = params[j];
            if (param.isArray()) {
                if (!byte[].class.equals(param)) {
                    param = param.getComponentType();
                    if (param.isPrimitive()) {
                        method = null;
                        break;
                    }
                }
            }
            Type methodParamType = Types.getParameterSQLType(param);
            if (methodParamType == null) {
                method = null;
                break;
            }
        }
        if (method == null) {
            continue;
        }
        if (java.sql.ResultSet.class.isAssignableFrom(method.getReturnType())) {
            list.add(methods[i]);
        } else {
            Type methodReturnType = Types.getParameterSQLType(method.getReturnType());
            if (methodReturnType != null) {
                list.add(methods[i]);
            }
        }
    }
    methods = new Method[list.size()];
    list.toArray(methods);
    return methods;
}
