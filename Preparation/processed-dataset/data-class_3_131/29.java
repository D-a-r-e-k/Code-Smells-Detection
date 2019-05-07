private Selectable[] generateSelectArrayForResultSet(Database db) throws AxionException {
    List list = new ArrayList(getSelectCount());
    TableIdentifier[] tables = getFromArray();
    for (int i = 0; i < getSelectCount(); i++) {
        Selectable sel = getSelect(i);
        if (sel instanceof ColumnIdentifier) {
            ColumnIdentifier colid = (ColumnIdentifier) sel;
            if ("*".equals(colid.getName())) {
                if (null == colid.getTableName()) {
                    for (int j = 0; j < getFromCount(); j++) {
                        TableIdentifier tableID = tables[j];
                        Table table = db.getTable(tableID);
                        for (Iterator iter = table.getColumnIdentifiers(); iter.hasNext(); ) {
                            ColumnIdentifier colId = (ColumnIdentifier) iter.next();
                            colId.setTableIdentifier(tableID);
                            list.add(colId);
                        }
                    }
                } else {
                    Table table = db.getTable(colid.getTableIdentifier());
                    for (Iterator iter = table.getColumnIdentifiers(); iter.hasNext(); ) {
                        list.add(iter.next());
                    }
                }
            } else {
                list.add(colid);
            }
        } else {
            list.add(sel);
        }
    }
    return (Selectable[]) (list.toArray(new Selectable[list.size()]));
}
