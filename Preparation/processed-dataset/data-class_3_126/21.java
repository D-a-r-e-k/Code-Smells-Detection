// Added in version 1.2 - sets the NDC Filter based on 
// a String passed in by the user.  This value is persisted 
// in the XML Configuration file. 
public void setNDCLogRecordFilter(String textFilter) {
    _table.getFilteredLogTableModel().setLogRecordFilter(createNDCLogRecordFilter(textFilter));
}
