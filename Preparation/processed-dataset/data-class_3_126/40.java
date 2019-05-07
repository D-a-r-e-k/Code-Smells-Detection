// Added in version 1.2 - Creates a new filter that sorts records based on 
// an NDC string passed in by the user. 
protected LogRecordFilter createNDCLogRecordFilter(String text) {
    _NDCTextFilter = text;
    LogRecordFilter result = new LogRecordFilter() {

        public boolean passes(LogRecord record) {
            String NDC = record.getNDC();
            CategoryPath path = new CategoryPath(record.getCategory());
            if (NDC == null || _NDCTextFilter == null) {
                return false;
            } else if (NDC.toLowerCase().indexOf(_NDCTextFilter.toLowerCase()) == -1) {
                return false;
            } else {
                return getMenuItem(record.getLevel()).isSelected() && _categoryExplorerTree.getExplorerModel().isCategoryPathActive(path);
            }
        }
    };
    return result;
}
