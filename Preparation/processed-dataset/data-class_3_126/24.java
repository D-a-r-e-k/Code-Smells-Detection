// Added in version 1.2 - Uses a different filter that sorts 
// based on an NDC string passed in by the user.  If the string 
// is null or is an empty string, we do nothing. 
protected void sortByNDC() {
    String text = _NDCTextFilter;
    if (text == null || text.length() == 0) {
        return;
    }
    // Use new NDC filter 
    _table.getFilteredLogTableModel().setLogRecordFilter(createNDCLogRecordFilter(text));
}
