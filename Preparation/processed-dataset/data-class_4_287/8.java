public void resolveReferences(Session session) {
    resolveRangeVariables(session);
    resolveColumnReferencesForAsterisk();
    finaliseColumns();
    resolveColumnReferences();
    unionColumnTypes = new Type[indexLimitVisible];
    setReferenceableColumns();
}
