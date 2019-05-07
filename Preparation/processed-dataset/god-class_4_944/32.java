private ArrayList sortColumns(ArrayList columns, ArrayList pKeys, Entity entity, String pKey) {
    ArrayList sortedColumns = new ArrayList();
    // Make sure the primary key will be the first field!  
    ArrayList primaryKeyColumns = new ArrayList();
    Column primaryKeyColumn = null;
    for (Iterator colIt = columns.iterator(); colIt.hasNext(); ) {
        Column column = (Column) colIt.next();
        if (pKeys.contains(column.getName())) {
            // We found the primary key column!  
            primaryKeyColumn = column;
            primaryKeyColumn.setPrimaryKey(true);
            primaryKeyColumns.add(primaryKeyColumn);
        } else {
            column.setPrimaryKey(false);
            sortedColumns.add(column);
        }
    }
    if (pKeys.size() > 1) {
        // We have a composite primary key!  
        entity.isCompositeCombo.setSelectedItem("true");
        String compositePK = entity.rootPackageText.getText() + "." + entity.nameText.getText() + "PK";
        entity.pKeyTypeText.setText(compositePK);
        entity.pKeyText.setText("");
    } else {
        entity.isCompositeCombo.setSelectedItem("false");
        if (pKeys.size() == 1) {
            entity.pKeyText.setText(Utils.format(pKey));
        }
    }
    // If a primary key column was found, we put it in front of the list.  
    columns = new ArrayList();
    if (primaryKeyColumn != null)
        columns.addAll(primaryKeyColumns);
    columns.addAll(sortedColumns);
    return columns;
}
