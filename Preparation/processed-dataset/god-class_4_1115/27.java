void resolveTypesPartOne(Session session) {
    resolveExpressionTypes(session, rowExpression);
    resolveAggregates();
    for (int i = 0; i < unionColumnTypes.length; i++) {
        unionColumnTypes[i] = Type.getAggregateType(unionColumnTypes[i], exprColumns[i].getDataType());
    }
}
