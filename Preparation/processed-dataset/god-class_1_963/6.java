public void setView(List columns) {
    TableColumnModel model = getColumnModel();
    // Remove all the columns: 
    for (int f = 0; f < _numCols; f++) {
        model.removeColumn(_tableColumns[f]);
    }
    Iterator selectedColumns = columns.iterator();
    Vector columnNameAndNumber = getColumnNameAndNumber();
    while (selectedColumns.hasNext()) {
        // add the column to the view 
        model.addColumn(_tableColumns[columnNameAndNumber.indexOf(selectedColumns.next())]);
    }
    //SWING BUG: 
    sizeColumnsToFit(-1);
}
