/******************************************************************************/
private void registerCheckBoxAction(JCheckBox cb) {
    cb.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            action_CheckBoxSwitch();
        }
    });
}
