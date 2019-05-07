protected JMenuItem createNoLogLevelsMenuItem() {
    JMenuItem result = new JMenuItem("Hide all LogLevels");
    result.setMnemonic('h');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            selectAllLogLevels(false);
            _table.getFilteredLogTableModel().refresh();
            updateStatusLabel();
        }
    });
    return result;
}
