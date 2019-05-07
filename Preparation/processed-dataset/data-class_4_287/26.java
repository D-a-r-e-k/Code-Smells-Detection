public void resolveTypes(Session session) {
    if (isResolved) {
        return;
    }
    resolveTypesPartOne(session);
    resolveTypesPartTwo(session);
    ArrayUtil.copyArray(resultTable.colTypes, unionColumnTypes, unionColumnTypes.length);
    for (int i = 0; i < indexStartHaving; i++) {
        if (exprColumns[i].dataType == null) {
            throw Error.error(ErrorCode.X_42567);
        }
    }
}
