// default view if a view is not set and saved 
public void setDetailedView() {
    //TODO: Defineable Views. 
    TableColumnModel model = getColumnModel();
    // Remove all the columns: 
    for (int f = 0; f < _numCols; f++) {
        model.removeColumn(_tableColumns[f]);
    }
    // Add them back in the correct order: 
    for (int i = 0; i < _numCols; i++) {
        model.addColumn(_tableColumns[i]);
    }
    //SWING BUG: 
    sizeColumnsToFit(-1);
}
