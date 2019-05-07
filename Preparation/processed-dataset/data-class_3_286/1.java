/**
     * Retrieves an INSERT Statement from this parse context.
     */
StatementDMQL compileInsertStatement(RangeVariable[] outerRanges) {
    read();
    readThis(Tokens.INTO);
    boolean[] columnCheckList;
    int[] columnMap;
    int colCount;
    Table table = readTableName();
    boolean overridingUser = false;
    boolean overridingSystem = false;
    boolean assignsToIdentity = false;
    columnCheckList = null;
    columnMap = table.getColumnMap();
    colCount = table.getColumnCount();
    int position = getPosition();
    if (!table.isInsertable() && !table.isTriggerInsertable() && !session.isProcessingScript) {
        throw Error.error(ErrorCode.X_42545);
    }
    Table baseTable = table.isTriggerInsertable() ? table : table.getBaseTable();
    switch(token.tokenType) {
        case Tokens.DEFAULT:
            {
                read();
                readThis(Tokens.VALUES);
                Expression insertExpression = new Expression(OpTypes.ROW, new Expression[] {});
                insertExpression = new Expression(OpTypes.TABLE, new Expression[] { insertExpression });
                columnCheckList = table.getNewColumnCheckList();
                for (int i = 0; i < table.colDefaults.length; i++) {
                    if (table.colDefaults[i] == null && table.identityColumn != i) {
                        if (!table.getColumn(i).isGenerated()) {
                            throw Error.error(ErrorCode.X_42544);
                        }
                    }
                }
                StatementDMQL cs = new StatementInsert(session, table, columnMap, insertExpression, columnCheckList, compileContext);
                return cs;
            }
        case Tokens.OPENBRACKET:
            {
                int brackets = readOpenBrackets();
                if (brackets == 1) {
                    boolean isQuery = false;
                    switch(token.tokenType) {
                        case Tokens.WITH:
                        case Tokens.SELECT:
                        case Tokens.TABLE:
                            {
                                rewind(position);
                                isQuery = true;
                                break;
                            }
                        default:
                    }
                    if (isQuery) {
                        break;
                    }
                    OrderedHashSet columnNames = new OrderedHashSet();
                    readSimpleColumnNames(columnNames, table);
                    readThis(Tokens.CLOSEBRACKET);
                    colCount = columnNames.size();
                    columnMap = table.getColumnIndexes(columnNames);
                    if (token.tokenType != Tokens.VALUES && token.tokenType != Tokens.OVERRIDING) {
                        break;
                    }
                } else {
                    rewind(position);
                    break;
                }
            }
        // fall through 
        case Tokens.OVERRIDING:
            {
                if (token.tokenType == Tokens.OVERRIDING) {
                    read();
                    if (token.tokenType == Tokens.USER) {
                        read();
                        overridingUser = true;
                    } else if (token.tokenType == Tokens.SYSTEM) {
                        read();
                        overridingSystem = true;
                    } else {
                        unexpectedToken();
                    }
                    readThis(Tokens.VALUE);
                    if (token.tokenType != Tokens.VALUES) {
                        break;
                    }
                }
            }
        // fall through 
        case Tokens.VALUES:
            {
                read();
                columnCheckList = table.getColumnCheckList(columnMap);
                Expression insertExpressions = XreadContextuallyTypedTable(colCount);
                HsqlList unresolved = insertExpressions.resolveColumnReferences(outerRanges, null);
                ExpressionColumn.checkColumnsResolved(unresolved);
                insertExpressions.resolveTypes(session, null);
                setParameterTypes(insertExpressions, table, columnMap);
                if (table != baseTable) {
                    int[] baseColumnMap = table.getBaseTableColumnMap();
                    int[] newColumnMap = new int[columnMap.length];
                    ArrayUtil.projectRow(baseColumnMap, columnMap, newColumnMap);
                    columnMap = newColumnMap;
                }
                Expression[] rowList = insertExpressions.nodes;
                for (int j = 0; j < rowList.length; j++) {
                    Expression[] rowArgs = rowList[j].nodes;
                    for (int i = 0; i < rowArgs.length; i++) {
                        Expression e = rowArgs[i];
                        ColumnSchema column = baseTable.getColumn(columnMap[i]);
                        if (column.isIdentity()) {
                            assignsToIdentity = true;
                            if (e.getType() != OpTypes.DEFAULT) {
                                if (table.identitySequence.isAlways()) {
                                    if (!overridingUser && !overridingSystem) {
                                        throw Error.error(ErrorCode.X_42543);
                                    }
                                }
                                if (overridingUser) {
                                    rowArgs[i] = new ExpressionColumn(OpTypes.DEFAULT);
                                }
                            }
                        } else if (column.hasDefault()) {
                        } else if (column.isGenerated()) {
                            if (e.getType() != OpTypes.DEFAULT) {
                                throw Error.error(ErrorCode.X_42541);
                            }
                        } else {
                            if (e.getType() == OpTypes.DEFAULT) {
                                throw Error.error(ErrorCode.X_42544);
                            }
                        }
                        if (e.isUnresolvedParam()) {
                            e.setAttributesAsColumn(column, true);
                        }
                    }
                }
                if (!assignsToIdentity && (overridingUser || overridingSystem)) {
                    unexpectedTokenRequire(Tokens.T_OVERRIDING);
                }
                StatementDMQL cs = new StatementInsert(session, table, columnMap, insertExpressions, columnCheckList, compileContext);
                return cs;
            }
        case Tokens.WITH:
        case Tokens.SELECT:
        case Tokens.TABLE:
            {
                break;
            }
        default:
            {
                throw unexpectedToken();
            }
    }
    columnCheckList = table.getColumnCheckList(columnMap);
    if (baseTable != null && table != baseTable) {
        int[] baseColumnMap = table.getBaseTableColumnMap();
        int[] newColumnMap = new int[columnMap.length];
        ArrayUtil.projectRow(baseColumnMap, columnMap, newColumnMap);
        columnMap = newColumnMap;
    }
    int enforcedDefaultIndex = table.getIdentityColumnIndex();
    int overrideIndex = -1;
    if (enforcedDefaultIndex != -1 && ArrayUtil.find(columnMap, enforcedDefaultIndex) > -1) {
        if (table.identitySequence.isAlways()) {
            if (!overridingUser && !overridingSystem) {
                throw Error.error(ErrorCode.X_42543);
            }
        }
        if (overridingUser) {
            overrideIndex = enforcedDefaultIndex;
        }
    } else if (overridingUser || overridingSystem) {
        unexpectedTokenRequire(Tokens.T_OVERRIDING);
    }
    Type[] types = new Type[columnMap.length];
    ArrayUtil.projectRow(baseTable.getColumnTypes(), columnMap, types);
    QueryExpression queryExpression = XreadQueryExpression();
    queryExpression.setReturningResult();
    queryExpression.resolve(session, outerRanges, types);
    if (colCount != queryExpression.getColumnCount()) {
        throw Error.error(ErrorCode.X_42546);
    }
    StatementDMQL cs = new StatementInsert(session, table, columnMap, columnCheckList, queryExpression, compileContext, overrideIndex);
    return cs;
}
