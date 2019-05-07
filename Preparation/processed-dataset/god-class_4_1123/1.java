void setupChecks() {
    if (targetTable != baseTable) {
        QuerySpecification select = ((TableDerived) targetTable).getQueryExpression().getMainSelect();
        this.updatableTableCheck = select.checkQueryCondition;
        this.checkRangeVariable = select.rangeVariables[0];
    }
}
