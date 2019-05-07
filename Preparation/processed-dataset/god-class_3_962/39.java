protected LogRecordFilter createLogRecordFilter() {
    LogRecordFilter result = new LogRecordFilter() {

        public boolean passes(LogRecord record) {
            CategoryPath path = new CategoryPath(record.getCategory());
            return getMenuItem(record.getLevel()).isSelected() && _categoryExplorerTree.getExplorerModel().isCategoryPathActive(path);
        }
    };
    return result;
}
