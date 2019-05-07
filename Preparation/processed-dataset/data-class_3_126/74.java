protected JMenuItem createConfigureReset() {
    JMenuItem result = new JMenuItem("Reset");
    result.setMnemonic('r');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            resetConfiguration();
        }
    });
    return result;
}
