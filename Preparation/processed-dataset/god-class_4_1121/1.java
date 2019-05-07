public Result execute(Session session) {
    Result result;
    try {
        result = getResult(session);
    } catch (Throwable t) {
        result = Result.newErrorResult(t, null);
    }
    if (result.isError()) {
        result.getException().setStatementType(group, type);
        return result;
    }
    session.database.schemaManager.setSchemaChangeTimestamp();
    try {
        if (isLogged) {
            session.database.logger.writeToLog(session, sql);
        }
    } catch (Throwable e) {
        return Result.newErrorResult(e, sql);
    }
    return result;
}
