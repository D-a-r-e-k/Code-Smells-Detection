//}}} 
// {{{ initComponents() 
/**
     * @param JList containing contents of log file
     * Arranges all the components of the GUI
     * @since jsXe 0.3pre15
     */
private void initComponents() {
    //GEN-BEGIN:initComponents 
    iconJLabel = new javax.swing.JLabel();
    //setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); 
    JPanel content = new JPanel(new BorderLayout(12, 12));
    content.setBorder(new EmptyBorder(12, 12, 12, 12));
    setContentPane(content);
    iconJLabel.setText(Messages.getMessage("ActivityLogDialog.Dialog.Message"));
    iconJLabel.setIcon(new javax.swing.ImageIcon(DirtyFilesDialog.class.getResource("/net/sourceforge/jsxe/icons/metal-Inform.png")));
    getContentPane().add(iconJLabel, BorderLayout.NORTH);
    getContentPane().add(activityLogJScrollPane, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(Box.createGlue());
    close = new JButton(Messages.getMessage("common.close"));
    close.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            okayJButtonActionPerformed(evt);
        }
    });
    getRootPane().setDefaultButton(close);
    buttonPanel.add(close);
    buttonPanel.add(Box.createGlue());
    getContentPane().add(BorderLayout.SOUTH, buttonPanel);
}
