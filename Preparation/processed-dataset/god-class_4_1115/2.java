// range variable sub queries are resolves fully 
private void resolveRangeVariables(Session session) {
    if (rangeVariables == null || rangeVariables.length < rangeVariableList.size()) {
        rangeVariables = new RangeVariable[rangeVariableList.size()];
        rangeVariableList.toArray(rangeVariables);
    }
    for (int i = 0; i < rangeVariables.length; i++) {
        rangeVariables[i].resolveRangeTable(session, rangeVariables, i);
    }
}
