protected JMenuItem createAllLogLevelsMenuItem() {
    JMenuItem result = new JMenuItem("Show all LogLevels");
    result.setMnemonic('s');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            selectAllLogLevels(true);
            _table.getFilteredLogTableModel().refresh();
            updateStatusLabel();
        }
    });
    return result;
}
