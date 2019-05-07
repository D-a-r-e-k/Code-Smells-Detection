public void resolveCheckOrGenExpression(Session session, RangeVariable[] ranges, boolean isCheck) {
    boolean nonDeterministic = false;
    OrderedHashSet set = new OrderedHashSet();
    HsqlList unresolved = resolveColumnReferences(ranges, null);
    ExpressionColumn.checkColumnsResolved(unresolved);
    resolveTypes(session, null);
    collectAllExpressions(set, Expression.subqueryAggregateExpressionSet, Expression.emptyExpressionSet);
    if (!set.isEmpty()) {
        throw Error.error(ErrorCode.X_42512);
    }
    collectAllExpressions(set, Expression.functionExpressionSet, Expression.emptyExpressionSet);
    for (int i = 0; i < set.size(); i++) {
        Expression current = (Expression) set.get(i);
        if (current.opType == OpTypes.FUNCTION) {
            if (!((FunctionSQLInvoked) current).isDeterministic()) {
                throw Error.error(ErrorCode.X_42512);
            }
        }
        if (current.opType == OpTypes.SQL_FUNCTION) {
            if (!((FunctionSQL) current).isDeterministic()) {
                if (isCheck) {
                    nonDeterministic = true;
                    continue;
                }
                throw Error.error(ErrorCode.X_42512);
            }
        }
    }
    if (isCheck && nonDeterministic) {
        HsqlArrayList list = new HsqlArrayList();
        RangeVariableResolver.decomposeAndConditions(this, list);
        for (int i = 0; i < list.size(); i++) {
            nonDeterministic = true;
            Expression e = (Expression) list.get(i);
            Expression e1;
            if (e instanceof ExpressionLogical) {
                boolean b = ((ExpressionLogical) e).convertToSmaller();
                if (!b) {
                    break;
                }
                e1 = e.getRightNode();
                e = e.getLeftNode();
                if (!e.dataType.isDateTimeType()) {
                    nonDeterministic = true;
                    break;
                }
                if (e.hasNonDeterministicFunction()) {
                    nonDeterministic = true;
                    break;
                }
                // both sides are actually consistent regarding timeZone 
                // e.dataType.isDateTimeTypeWithZone(); 
                if (e1 instanceof ExpressionArithmetic) {
                    if (opType == OpTypes.ADD) {
                        if (e1.getRightNode().hasNonDeterministicFunction()) {
                            e1.swapLeftAndRightNodes();
                        }
                    } else if (opType == OpTypes.SUBTRACT) {
                    } else {
                        break;
                    }
                    if (e1.getRightNode().hasNonDeterministicFunction()) {
                        break;
                    }
                    e1 = e1.getLeftNode();
                }
                if (e1.opType == OpTypes.SQL_FUNCTION) {
                    FunctionSQL function = (FunctionSQL) e1;
                    switch(function.funcType) {
                        case FunctionSQL.FUNC_CURRENT_DATE:
                        case FunctionSQL.FUNC_CURRENT_TIMESTAMP:
                        case FunctionSQL.FUNC_LOCALTIMESTAMP:
                            nonDeterministic = false;
                            continue;
                        default:
                            break;
                    }
                    break;
                }
                break;
            } else {
                break;
            }
        }
        if (nonDeterministic) {
            throw Error.error(ErrorCode.X_42512);
        }
    }
    set.clear();
    collectObjectNames(set);
    for (int i = 0; i < set.size(); i++) {
        HsqlName name = (HsqlName) set.get(i);
        switch(name.type) {
            case SchemaObject.COLUMN:
                {
                    if (isCheck) {
                        break;
                    }
                    int colIndex = ranges[0].rangeTable.findColumn(name.name);
                    ColumnSchema column = ranges[0].rangeTable.getColumn(colIndex);
                    if (column.isGenerated()) {
                        throw Error.error(ErrorCode.X_42512);
                    }
                    break;
                }
            case SchemaObject.SEQUENCE:
                {
                    throw Error.error(ErrorCode.X_42512);
                }
            case SchemaObject.SPECIFIC_ROUTINE:
                {
                    Routine routine = (Routine) session.database.schemaManager.getSchemaObject(name);
                    if (!routine.isDeterministic()) {
                        throw Error.error(ErrorCode.X_42512);
                    }
                    int impact = routine.getDataImpact();
                    if (impact == Routine.READS_SQL || impact == Routine.MODIFIES_SQL) {
                        throw Error.error(ErrorCode.X_42512);
                    }
                }
        }
    }
    set.clear();
}
