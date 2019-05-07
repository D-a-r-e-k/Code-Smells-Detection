/**
     * Parses a WHEN clause from a MERGE statement. This can be either a
     * WHEN MATCHED or WHEN NOT MATCHED clause, or both, and the appropriate
     * values will be updated.
     *
     * If the var that is to hold the data is not null, then we already
     * encountered this type of clause, which is only allowed once, and at least
     * one is required.
     */
private void readMergeWhen(LongDeque updateColIndexList, OrderedHashSet insertColumnNames, OrderedHashSet updateTargetSet, HsqlArrayList insertExpressions, HsqlArrayList updateExpressions, RangeVariable[] targetRangeVars, RangeVariable sourceRangeVar) {
    Table table = targetRangeVars[0].rangeTable;
    int columnCount = table.getColumnCount();
    readThis(Tokens.WHEN);
    if (token.tokenType == Tokens.MATCHED) {
        if (updateExpressions.size() != 0) {
            throw Error.error(ErrorCode.X_42547);
        }
        read();
        readThis(Tokens.THEN);
        readThis(Tokens.UPDATE);
        readThis(Tokens.SET);
        readSetClauseList(targetRangeVars, updateTargetSet, updateColIndexList, updateExpressions);
    } else if (token.tokenType == Tokens.NOT) {
        if (insertExpressions.size() != 0) {
            throw Error.error(ErrorCode.X_42548);
        }
        read();
        readThis(Tokens.MATCHED);
        readThis(Tokens.THEN);
        readThis(Tokens.INSERT);
        // parse INSERT statement 
        // optional column list 
        int brackets = readOpenBrackets();
        if (brackets == 1) {
            readSimpleColumnNames(insertColumnNames, targetRangeVars[0]);
            columnCount = insertColumnNames.size();
            readThis(Tokens.CLOSEBRACKET);
            brackets = 0;
        }
        readThis(Tokens.VALUES);
        Expression e = XreadContextuallyTypedTable(columnCount);
        if (e.nodes.length != 1) {
            throw Error.error(ErrorCode.X_21000);
        }
        insertExpressions.add(e);
    } else {
        throw unexpectedToken();
    }
    if (token.tokenType == Tokens.WHEN) {
        readMergeWhen(updateColIndexList, insertColumnNames, updateTargetSet, insertExpressions, updateExpressions, targetRangeVars, sourceRangeVar);
    }
}
