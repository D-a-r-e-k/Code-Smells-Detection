public synchronized void clearLogRecords() {
    //For JDK1.3 
    //((DefaultTableModel)getModel()).setRowCount(0); 
    // For JDK1.2.x 
    getFilteredLogTableModel().clear();
}
