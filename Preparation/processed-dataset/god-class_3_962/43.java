protected void addTableModelProperties() {
    final FilteredLogTableModel model = _table.getFilteredLogTableModel();
    addDisplayedProperty(new Object() {

        public String toString() {
            return getRecordsDisplayedMessage();
        }
    });
    addDisplayedProperty(new Object() {

        public String toString() {
            return "Maximum number of displayed LogRecords: " + model._maxNumberOfLogRecords;
        }
    });
}
