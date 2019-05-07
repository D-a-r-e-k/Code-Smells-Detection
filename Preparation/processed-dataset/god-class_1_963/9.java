// assign a column number to a column name 
protected Vector getColumnNameAndNumber() {
    Vector columnNameAndNumber = new Vector();
    for (int i = 0; i < _colNames.length; i++) {
        columnNameAndNumber.add(i, _colNames[i]);
    }
    return columnNameAndNumber;
}
