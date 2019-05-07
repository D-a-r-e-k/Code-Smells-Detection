protected JMenuItem createExitMI() {
    JMenuItem result = new JMenuItem("Exit");
    result.setMnemonic('x');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            requestExit();
        }
    });
    return result;
}
