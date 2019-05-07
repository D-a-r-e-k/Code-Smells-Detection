protected JMenuItem createHelpProperties() {
    final String title = "LogFactor5 Properties";
    final JMenuItem result = new JMenuItem(title);
    result.setMnemonic('l');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            showPropertiesDialog(title);
        }
    });
    return result;
}
