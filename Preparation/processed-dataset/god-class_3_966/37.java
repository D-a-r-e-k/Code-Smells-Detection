private void populateColumnIdToFieldMap(Map indexMap, TableIdentifier tableIdent, int offset, Database db) throws AxionException {
    Table table = db.getTable(tableIdent);
    if (null == table) {
        throw new AxionException("Table " + tableIdent + " not found.");
    }
    for (int j = 0, J = table.getColumnCount(); j < J; j++) {
        ColumnIdentifier id = null;
        // determine which selected column id matches, if any 
        for (int k = 0, K = getSelectCount(); k < K; k++) {
            Selectable sel = getSelect(k);
            if (sel instanceof ColumnIdentifier) {
                ColumnIdentifier cSel = (ColumnIdentifier) sel;
                if (tableIdent.equals(cSel.getTableIdentifier()) && cSel.getName().equals(table.getColumn(j).getName())) {
                    id = cSel;
                    break;
                }
            }
        }
        if (null == id) {
            id = new ColumnIdentifier(tableIdent, table.getColumn(j).getName());
        }
        indexMap.put(id, new Integer(offset + j));
    }
}
