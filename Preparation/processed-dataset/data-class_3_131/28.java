private RowIterator processTable(TableIdentifier tableident, Database db, ColumnIdentifier col, int pos) throws AxionException {
    Table table = db.getTable(tableident);
    // Create a references to the RowIterator for this table. 
    RowIterator tableiter = null;
    // And create  the set of WhereNodes that apply to this table. 
    Set whereNodesForTable = null;
    if (col != null && pos == 1) {
        Index index = table.getIndexForColumn(table.getColumn(col.getName()));
        if (index != null) {
            tableiter = new ChangingIndexedRowIterator(index, table, ComparisonOperator.EQUAL);
        }
    }
    if (_applyWhereNodesAfterJoin == false) {
        for (Iterator whereNodeIter = _unappliedWhereNodes.iterator(); whereNodeIter.hasNext(); ) {
            WhereNode node = (WhereNode) (whereNodeIter.next());
            // If the node only references this table... 
            if (onlyReferencesTable(tableident, node)) {
                // .. and we haven't yet applied an index,... 
                if (null == tableiter) {
                    // ...then try to find an index for this node. 
                    tableiter = table.getIndexedRows(node, true);
                    // If we still don't have an iterator, 
                    // then no index is available, so add the node 
                    // to the whereNodesForTable set... 
                    if (null == tableiter) {
                        if (null == whereNodesForTable) {
                            whereNodesForTable = new HashSet();
                        }
                        whereNodesForTable.add(node);
                    }
                } else {
                    // Else if we've already applied an index, 
                    // then add the node to the whereNodesForTable 
                    if (null == whereNodesForTable) {
                        whereNodesForTable = new HashSet();
                    }
                    whereNodesForTable.add(node);
                }
                // Remove the WhereNode from the unapplied where nodes,  
                // since we've either added it to the whereNodesForTable set, 
                // or we applied it via the index. 
                whereNodeIter.remove();
            }
        }
    }
    // If we still don't have a RowIterator for this table, 
    // then we'll use a table scan. 
    if (null == tableiter) {
        tableiter = table.getRowIterator(true);
    }
    // Apply any unapplied whereNodesForTable. 
    if (null != whereNodesForTable && !whereNodesForTable.isEmpty()) {
        Map localmap = new HashMap();
        populateColumnIdToFieldMap(localmap, tableident, 0, db);
        Iterator whereNodesForTableIter = whereNodesForTable.iterator();
        while (whereNodesForTableIter.hasNext()) {
            WhereNode node = (WhereNode) (whereNodesForTableIter.next());
            tableiter = new FilteringRowIterator(tableiter, new RowDecorator(localmap), node);
        }
    }
    populateColumnIdToFieldMap(_colIdToFieldMap, tableident, _indexOffset, db);
    _indexOffset += table.getColumnCount();
    return (tableiter);
}
