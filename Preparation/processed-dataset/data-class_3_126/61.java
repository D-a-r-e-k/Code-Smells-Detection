protected JCheckBoxMenuItem createLogTableColumnMenuItem(LogTableColumn column) {
    JCheckBoxMenuItem result = new JCheckBoxMenuItem(column.toString());
    result.setSelected(true);
    result.setMnemonic(column.toString().charAt(0));
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            // update list of columns and reset the view 
            List selectedColumns = updateView();
            _table.setView(selectedColumns);
        }
    });
    return result;
}
