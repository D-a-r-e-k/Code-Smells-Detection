Result invokeJavaMethod(Session session, Object[] data) {
    Result result;
    try {
        if (dataImpact == Routine.NO_SQL) {
            session.sessionContext.isReadOnly = Boolean.TRUE;
            session.setNoSQL();
        } else if (dataImpact == Routine.CONTAINS_SQL) {
            session.sessionContext.isReadOnly = Boolean.TRUE;
        } else if (dataImpact == Routine.READS_SQL) {
            session.sessionContext.isReadOnly = Boolean.TRUE;
        }
        Object returnValue = javaMethod.invoke(null, data);
        if (returnsTable()) {
            if (returnValue instanceof JDBCResultSet) {
                result = ((JDBCResultSet) returnValue).result;
            } else {
                // convert ResultSet to table 
                throw Error.runtimeError(ErrorCode.U_S0500, "FunctionSQLInvoked");
            }
        } else {
            returnValue = returnType.convertJavaToSQL(session, returnValue);
            result = Result.newPSMResult(returnValue);
        }
    } catch (InvocationTargetException e) {
        result = Result.newErrorResult(Error.error(ErrorCode.X_46000, getName().name), null);
    } catch (IllegalAccessException e) {
        result = Result.newErrorResult(Error.error(ErrorCode.X_46000, getName().name), null);
    } catch (Throwable e) {
        result = Result.newErrorResult(Error.error(ErrorCode.X_46000, getName().name), null);
    }
    return result;
}
