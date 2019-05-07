protected JMenuItem createConfigureSave() {
    JMenuItem result = new JMenuItem("Save");
    result.setMnemonic('s');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            saveConfiguration();
        }
    });
    return result;
}
