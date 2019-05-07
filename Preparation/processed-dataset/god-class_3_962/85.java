// Added version 1.2 - Allows users to Sort Log Records by an 
// NDC text filter. A new LogRecordFilter was created to 
// sort the records. 
protected JMenuItem createEditSortNDCMI() {
    JMenuItem editSortNDCMI = new JMenuItem("Sort by NDC");
    editSortNDCMI.setMnemonic('s');
    editSortNDCMI.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String inputValue = JOptionPane.showInputDialog(_logMonitorFrame, "Sort by this NDC: ", "Sort Log Records by NDC", JOptionPane.QUESTION_MESSAGE);
            setNDCTextFilter(inputValue);
            sortByNDC();
            _table.getFilteredLogTableModel().refresh();
            updateStatusLabel();
        }
    });
    return editSortNDCMI;
}
