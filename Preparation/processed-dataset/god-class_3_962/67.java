/**
   * Menu item added to allow log files to be opened with
   * the LF5 GUI.
   */
protected JMenuItem createOpenMI() {
    JMenuItem result = new JMenuItem("Open...");
    result.setMnemonic('o');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            requestOpen();
        }
    });
    return result;
}
