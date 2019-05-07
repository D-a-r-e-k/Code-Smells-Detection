/**
   * Menu item added to allow log files loaded from a URL
   * to be opened by the LF5 GUI.
   */
protected JMenuItem createOpenURLMI() {
    JMenuItem result = new JMenuItem("Open URL...");
    result.setMnemonic('u');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            requestOpenURL();
        }
    });
    return result;
}
