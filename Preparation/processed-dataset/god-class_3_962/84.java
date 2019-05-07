protected JMenuItem createEditFindMI() {
    JMenuItem editFindMI = new JMenuItem("Find");
    editFindMI.setMnemonic('f');
    editFindMI.setAccelerator(KeyStroke.getKeyStroke("control F"));
    editFindMI.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String inputValue = JOptionPane.showInputDialog(_logMonitorFrame, "Find text: ", "Search Record Messages", JOptionPane.QUESTION_MESSAGE);
            setSearchText(inputValue);
            findSearchText();
        }
    });
    return editFindMI;
}
