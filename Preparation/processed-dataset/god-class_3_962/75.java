protected JMenuItem createConfigureMaxRecords() {
    JMenuItem result = new JMenuItem("Set Max Number of Records");
    result.setMnemonic('m');
    result.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            setMaxRecordConfiguration();
        }
    });
    return result;
}
