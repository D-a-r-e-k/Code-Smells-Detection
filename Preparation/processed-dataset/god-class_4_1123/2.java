Result getResult(Session session) {
    Result result = null;
    switch(type) {
        case StatementTypes.UPDATE_WHERE:
            result = executeUpdateStatement(session);
            break;
        case StatementTypes.MERGE:
            result = executeMergeStatement(session);
            break;
        case StatementTypes.DELETE_WHERE:
            if (isTruncate) {
                result = executeDeleteTruncateStatement(session);
            } else {
                result = executeDeleteStatement(session);
            }
            break;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "StatementDML");
    }
    return result;
}
