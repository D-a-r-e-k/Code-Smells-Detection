private void setRangeVariableConditions(Session session) {
    RangeVariableResolver rangeResolver = new RangeVariableResolver(rangeVariables, queryCondition, compileContext);
    rangeResolver.processConditions(session);
    rangeVariables = rangeResolver.rangeVariables;
}
