protected JCheckBoxMenuItem createMenuItem(LogLevel level) {
    JCheckBoxMenuItem result = new JCheckBoxMenuItem(level.toString());
    result.setSelected(true);
    result.setMnemonic(level.toString().charAt(0));
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            _table.getFilteredLogTableModel().refresh();
            updateStatusLabel();
        }
    });
    return result;
}
