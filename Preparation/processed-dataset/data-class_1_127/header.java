void method0() { 
private static final long serialVersionUID = 4867085140195148458L;
//-------------------------------------------------------------------------- 
//   Constants: 
//-------------------------------------------------------------------------- 
//-------------------------------------------------------------------------- 
//   Protected Variables: 
//-------------------------------------------------------------------------- 
protected int _rowHeight = 30;
protected JTextArea _detailTextArea;
// For the columns: 
protected int _numCols = 9;
protected TableColumn[] _tableColumns = new TableColumn[_numCols];
protected int[] _colWidths = { 40, 40, 40, 70, 70, 360, 440, 200, 60 };
protected LogTableColumn[] _colNames = LogTableColumn.getLogTableColumnArray();
protected int _colDate = 0;
protected int _colThread = 1;
protected int _colMessageNum = 2;
protected int _colLevel = 3;
protected int _colNDC = 4;
protected int _colCategory = 5;
protected int _colMessage = 6;
protected int _colLocation = 7;
protected int _colThrown = 8;
protected DateFormatManager _dateFormatManager = null;
}
