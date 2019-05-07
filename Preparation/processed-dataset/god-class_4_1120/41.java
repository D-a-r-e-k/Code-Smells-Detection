/**
     * Details of IN condition optimisation for 1.9.0
     * Predicates with SELECT are QUERY expressions
     *
     * Predicates with IN list
     *
     * Parser adds a SubQuery to the list for each predicate
     * At type resolution IN lists that are entirely fixed constant or parameter
     * values are selected for possible optimisation. The flags:
     *
     * IN expression right side isCorrelated == true if there are non-constant,
     * non-param expressions in the list (Expressions may have to be resolved
     * against the full set of columns of the query, so must be re-evaluated
     * for each row and evaluated after all the joins have been made)
     *
     * VALUELIST expression isFixedConstantValueList == true when all
     * expressions are fixed constant and none is a param. With this flag,
     * a single-column VALUELIST can be accessed as a HashMap.
     *
     * Predicates may be optimised as joins if isCorrelated == false
     *
     */
void insertValuesIntoSubqueryTable(Session session, PersistentStore store) {
    for (int i = 0; i < nodes.length; i++) {
        Object[] data = nodes[i].getRowValue(session);
        for (int j = 0; j < nodeDataTypes.length; j++) {
            data[j] = nodeDataTypes[j].convertToType(session, data[j], nodes[i].nodes[j].dataType);
        }
        Row row = (Row) store.getNewCachedObject(session, data);
        try {
            store.indexRow(session, row);
        } catch (HsqlException e) {
        }
    }
}
