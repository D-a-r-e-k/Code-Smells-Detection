protected JComboBox createLogLevelCombo() {
    JComboBox result = new JComboBox();
    Iterator levels = getLogLevels();
    while (levels.hasNext()) {
        result.addItem(levels.next());
    }
    result.setSelectedItem(_leastSevereDisplayedLogLevel);
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            JComboBox box = (JComboBox) e.getSource();
            LogLevel level = (LogLevel) box.getSelectedItem();
            setLeastSevereDisplayedLogLevel(level);
        }
    });
    result.setMaximumSize(result.getPreferredSize());
    return result;
}
