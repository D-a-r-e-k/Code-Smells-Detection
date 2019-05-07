protected JMenuItem createResetLogLevelColorMenuItem() {
    JMenuItem result = new JMenuItem("Reset LogLevel Colors");
    result.setMnemonic('r');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            // reset the level colors in the map 
            LogLevel.resetLogLevelColorMap();
            // refresh the table 
            _table.getFilteredLogTableModel().refresh();
        }
    });
    return result;
}
