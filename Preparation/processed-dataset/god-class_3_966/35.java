private boolean onlyReferencesTable(TableIdentifier table, WhereNode node) {
    ReferencesOtherTablesWhereNodeVisitor v = new ReferencesOtherTablesWhereNodeVisitor(table);
    v.visit(node);
    return v.getResult();
}
