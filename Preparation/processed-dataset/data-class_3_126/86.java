// Added in version 1.2 - Resets the LogRecordFilter back to default 
// filter. 
protected JMenuItem createEditRestoreAllNDCMI() {
    JMenuItem editRestoreAllNDCMI = new JMenuItem("Restore all NDCs");
    editRestoreAllNDCMI.setMnemonic('r');
    editRestoreAllNDCMI.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _table.getFilteredLogTableModel().setLogRecordFilter(createLogRecordFilter());
            // reset the text filter 
            setNDCTextFilter("");
            _table.getFilteredLogTableModel().refresh();
            updateStatusLabel();
        }
    });
    return editRestoreAllNDCMI;
}
