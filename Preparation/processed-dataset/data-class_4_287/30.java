private void resolveGroups() {
    // - 1.9.0 is standard compliant but has more extended support for 
    //   referencing columns 
    // - check there is no direct aggregate expression in group by 
    // - check each expression in select list can be 
    //   decomposed into the expressions in group by or any aggregates 
    //   this allows direct function of group by expressions, but 
    //   doesn't allow indirect functions. e.g. 
    //     select 2*abs(cola) , sum(colb) from t group by abs(cola) // ok 
    //     select 2*(cola + 10) , sum(colb) from t group by cola + 10 // ok 
    //     select abs(cola) , sum(colb) from t group by cola // ok 
    //     select 2*cola + 20 , sum(colb) from t group by cola + 10 // not allowed although correct 
    //     select cola , sum(colb) from t group by abs(cola) // not allowed because incorrect 
    // - group by can introduce invisible, derived columns into the query table 
    // - check the having expression can be decomposed into 
    //   select list expresions plus group by expressions 
    // - having cannot introduce additional, derived columns 
    // - having cannot reference columns not in the select or group by list 
    // - if there is any aggregate in select list but no group by, no 
    //   non-aggregates is allowed 
    // - check order by columns 
    // - if distinct select, order by must be composed of the select list columns 
    // - if grouped by, then order by should be decomposed into the 
    //   select list plus group by list 
    // - references to column aliases are allowed only in order by (Standard 
    //   compliance) and take precendence over references to non-alias 
    //   column names. 
    // - references to table / correlation and column list in correlation 
    //   names are handled according to the Standard 
    //  fredt@users 
    tempSet.clear();
    if (isGrouped) {
        for (int i = indexLimitVisible; i < indexLimitVisible + groupByColumnCount; i++) {
            exprColumns[i].collectAllExpressions(tempSet, Expression.aggregateFunctionSet, Expression.subqueryExpressionSet);
            if (!tempSet.isEmpty()) {
                throw Error.error(ErrorCode.X_42572, ((Expression) tempSet.get(0)).getSQL());
            }
        }
        for (int i = 0; i < indexLimitVisible; i++) {
            if (!exprColumns[i].isComposedOf(exprColumns, indexLimitVisible, indexLimitVisible + groupByColumnCount, Expression.subqueryAggregateExpressionSet)) {
                tempSet.add(exprColumns[i]);
            }
        }
        if (!tempSet.isEmpty() && !resolveForGroupBy(tempSet)) {
            throw Error.error(ErrorCode.X_42574, ((Expression) tempSet.get(0)).getSQL());
        }
    } else if (isAggregated) {
        for (int i = 0; i < indexLimitVisible; i++) {
            exprColumns[i].collectAllExpressions(tempSet, Expression.columnExpressionSet, Expression.aggregateFunctionSet);
            if (!tempSet.isEmpty()) {
                throw Error.error(ErrorCode.X_42574, ((Expression) tempSet.get(0)).getSQL());
            }
        }
    }
    tempSet.clear();
    if (havingCondition != null) {
        if (unresolvedExpressions != null) {
            tempSet.addAll(unresolvedExpressions);
        }
        for (int i = indexLimitVisible; i < indexLimitVisible + groupByColumnCount; i++) {
            tempSet.add(exprColumns[i]);
        }
        if (!havingCondition.isComposedOf(tempSet, Expression.subqueryAggregateExpressionSet)) {
            throw Error.error(ErrorCode.X_42573);
        }
        tempSet.clear();
    }
    if (isDistinctSelect) {
        int orderCount = sortAndSlice.getOrderLength();
        for (int i = 0; i < orderCount; i++) {
            Expression e = (Expression) sortAndSlice.exprList.get(i);
            if (e.queryTableColumnIndex != -1) {
                continue;
            }
            if (!e.isComposedOf(exprColumns, 0, indexLimitVisible, Expression.emptyExpressionSet)) {
                throw Error.error(ErrorCode.X_42576);
            }
        }
    }
    if (isGrouped) {
        int orderCount = sortAndSlice.getOrderLength();
        for (int i = 0; i < orderCount; i++) {
            Expression e = (Expression) sortAndSlice.exprList.get(i);
            if (e.queryTableColumnIndex != -1) {
                continue;
            }
            if (!e.isAggregate() && !e.isComposedOf(exprColumns, 0, indexLimitVisible + groupByColumnCount, Expression.emptyExpressionSet)) {
                throw Error.error(ErrorCode.X_42576);
            }
        }
    }
    if (isDistinctSelect || isGrouped) {
        simpleLimit = false;
    }
    if (!isAggregated) {
        return;
    }
    OrderedHashSet expressions = new OrderedHashSet();
    OrderedHashSet columnExpressions = new OrderedHashSet();
    for (int i = indexStartAggregates; i < indexLimitExpressions; i++) {
        Expression e = exprColumns[i];
        Expression c = new ExpressionColumn(e, i, resultRangePosition);
        expressions.add(e);
        columnExpressions.add(c);
    }
    for (int i = 0; i < indexStartHaving; i++) {
        if (exprColumns[i].isAggregate()) {
            continue;
        }
        Expression e = exprColumns[i];
        if (expressions.add(e)) {
            Expression c = new ExpressionColumn(e, i, resultRangePosition);
            columnExpressions.add(c);
        }
    }
    // order by with aggregate 
    int orderCount = sortAndSlice.getOrderLength();
    for (int i = 0; i < orderCount; i++) {
        Expression e = (Expression) sortAndSlice.exprList.get(i);
        if (e.getLeftNode().isAggregate()) {
            e.setAggregate();
        }
    }
    for (int i = indexStartOrderBy; i < indexStartAggregates; i++) {
        if (exprColumns[i].getLeftNode().isAggregate()) {
            exprColumns[i].setAggregate();
        }
    }
    for (int i = 0; i < indexStartAggregates; i++) {
        Expression e = exprColumns[i];
        if (!e.isAggregate() && !e.isCorrelated()) {
            continue;
        }
        aggregateCheck[i] = true;
        if (e.isAggregate()) {
            e.convertToSimpleColumn(expressions, columnExpressions);
        }
    }
    for (int i = 0; i < aggregateSet.size(); i++) {
        Expression e = (Expression) aggregateSet.get(i);
        e.convertToSimpleColumn(expressions, columnExpressions);
    }
    if (resolvedSubqueryExpressions != null) {
        for (int i = 0; i < resolvedSubqueryExpressions.size(); i++) {
            Expression e = (Expression) resolvedSubqueryExpressions.get(i);
            e.convertToSimpleColumn(expressions, columnExpressions);
        }
    }
}
