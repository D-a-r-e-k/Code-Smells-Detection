/**
   * Add a log record message to be displayed in the LogTable.
   * This method is thread-safe as it posts requests to the SwingThread
   * rather than processing directly.
   */
public void addMessage(final LogRecord lr) {
    if (_isDisposed == true) {
        // If the frame has been disposed of, do not log any more 
        // messages. 
        return;
    }
    SwingUtilities.invokeLater(new Runnable() {

        public void run() {
            _categoryExplorerTree.getExplorerModel().addLogRecord(lr);
            _table.getFilteredLogTableModel().addLogRecord(lr);
            // update table 
            updateStatusLabel();
        }
    });
}
