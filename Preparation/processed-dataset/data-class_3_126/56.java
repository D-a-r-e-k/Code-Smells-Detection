protected JMenuItem createSubMenuItem(LogLevel level) {
    final JMenuItem result = new JMenuItem(level.toString());
    final LogLevel logLevel = level;
    result.setMnemonic(level.toString().charAt(0));
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            showLogLevelColorChangeDialog(result, logLevel);
        }
    });
    return result;
}
