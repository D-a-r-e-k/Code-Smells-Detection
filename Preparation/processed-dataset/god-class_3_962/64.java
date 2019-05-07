protected JMenuItem createNoLogTableColumnsMenuItem() {
    JMenuItem result = new JMenuItem("Hide all Columns");
    result.setMnemonic('h');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            selectAllLogTableColumns(false);
            // update list of columns and reset the view 
            List selectedColumns = updateView();
            _table.setView(selectedColumns);
        }
    });
    return result;
}
