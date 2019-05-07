/**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
// <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents  
private void initComponents() {
    panel = new javax.swing.JPanel();
    nameLabel = new javax.swing.JLabel();
    typeLabel = new javax.swing.JLabel();
    columnNameLabel = new javax.swing.JLabel();
    sqlTypeLabel = new javax.swing.JLabel();
    jdbcTypeLabel = new javax.swing.JLabel();
    nameText = new javax.swing.JTextField();
    typeText = new javax.swing.JTextField();
    columnNameText = new javax.swing.JTextField();
    sqlTypeText = new javax.swing.JTextField();
    jPanel1 = new javax.swing.JPanel();
    validationDependsText = new javax.swing.JTextField();
    validationXMLScrollPane = new javax.swing.JScrollPane();
    validationXMLTextArea = new javax.swing.JTextArea();
    validationXMLLabel = new javax.swing.JLabel();
    validationDependsLabel = new javax.swing.JLabel();
    regenerateButton = new javax.swing.JButton();
    jdbcTypeComboBox = new javax.swing.JComboBox();
    checkboxPanel2 = new javax.swing.JPanel();
    autoGeneratedCheckBox = new javax.swing.JCheckBox();
    requiredCheckBox = new javax.swing.JCheckBox();
    checkboxPanel1 = new javax.swing.JPanel();
    primaryKeyCheckBox = new javax.swing.JCheckBox();
    foreignKeyCheckBox = new javax.swing.JCheckBox();
    panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    nameLabel.setText("Name: ");
    panel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 90, -1));
    typeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    typeLabel.setText("Type: ");
    panel.add(typeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));
    columnNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    columnNameLabel.setText("Column name: ");
    panel.add(columnNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, -1));
    sqlTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    sqlTypeLabel.setText("SQL-type: ");
    panel.add(sqlTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, -1));
    jdbcTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    jdbcTypeLabel.setText("JDBC-type: ");
    panel.add(jdbcTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 90, -1));
    nameText.addFocusListener(new java.awt.event.FocusAdapter() {

        public void focusLost(java.awt.event.FocusEvent evt) {
            nameTextFocusLost(evt);
        }
    });
    panel.add(nameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 260, -1));
    typeText.addFocusListener(new java.awt.event.FocusAdapter() {

        public void focusLost(java.awt.event.FocusEvent evt) {
            typeTextFocusLost(evt);
        }
    });
    panel.add(typeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 260, -1));
    columnNameText.addFocusListener(new java.awt.event.FocusAdapter() {

        public void focusLost(java.awt.event.FocusEvent evt) {
            columnNameTextFocusLost(evt);
        }
    });
    panel.add(columnNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 260, -1));
    sqlTypeText.addFocusListener(new java.awt.event.FocusAdapter() {

        public void focusLost(java.awt.event.FocusEvent evt) {
            sqlTypeTextFocusLost(evt);
        }
    });
    panel.add(sqlTypeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 260, -1));
    jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    jPanel1.setBorder(new javax.swing.border.TitledBorder("Struts validations:"));
    validationDependsText.setColumns(46);
    validationDependsText.addFocusListener(new java.awt.event.FocusAdapter() {

        public void focusLost(java.awt.event.FocusEvent evt) {
            validationDependsTextFocusLost(evt);
        }
    });
    jPanel1.add(validationDependsText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 290, -1));
    validationXMLTextArea.setColumns(48);
    validationXMLTextArea.setFont(new java.awt.Font("Lucida Console", 0, 10));
    validationXMLTextArea.setRows(6);
    validationXMLTextArea.addFocusListener(new java.awt.event.FocusAdapter() {

        public void focusLost(java.awt.event.FocusEvent evt) {
            validationXMLTextAreaFocusLost(evt);
        }
    });
    validationXMLScrollPane.setViewportView(validationXMLTextArea);
    jPanel1.add(validationXMLScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 59, -1, 90));
    validationXMLLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    validationXMLLabel.setText("Validation XML: ");
    jPanel1.add(validationXMLLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 60, 110, 20));
    validationDependsLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    validationDependsLabel.setText("Validations: ");
    jPanel1.add(validationDependsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 30, 110, -1));
    regenerateButton.setText("Regenerate validations");
    regenerateButton.setEnabled(false);
    regenerateButton.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            regenerateButtonActionPerformed(evt);
        }
    });
    jPanel1.add(regenerateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, -1, -1));
    panel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 400, 200));
    jdbcTypeComboBox.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jdbcTypeComboBoxActionPerformed(evt);
        }
    });
    panel.add(jdbcTypeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 260, -1));
    checkboxPanel2.setLayout(new java.awt.BorderLayout());
    autoGeneratedCheckBox.setText("Auto-generated primary key:");
    autoGeneratedCheckBox.setEnabled(false);
    autoGeneratedCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    autoGeneratedCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    autoGeneratedCheckBox.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            autoGeneratedCheckBoxActionPerformed(evt);
        }
    });
    checkboxPanel2.add(autoGeneratedCheckBox, java.awt.BorderLayout.SOUTH);
    requiredCheckBox.setText("Required (not nullable):");
    requiredCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
    requiredCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    requiredCheckBox.setMaximumSize(new java.awt.Dimension(186, 24));
    requiredCheckBox.setMinimumSize(new java.awt.Dimension(186, 24));
    requiredCheckBox.setPreferredSize(new java.awt.Dimension(186, 24));
    requiredCheckBox.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            requiredCheckBoxActionPerformed(evt);
        }
    });
    checkboxPanel2.add(requiredCheckBox, java.awt.BorderLayout.NORTH);
    panel.add(checkboxPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 170, 190, 50));
    checkboxPanel1.setLayout(new java.awt.BorderLayout());
    primaryKeyCheckBox.setText("Primary key:");
    primaryKeyCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    primaryKeyCheckBox.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            primaryKeyCheckBoxActionPerformed(evt);
        }
    });
    checkboxPanel1.add(primaryKeyCheckBox, java.awt.BorderLayout.NORTH);
    foreignKeyCheckBox.setText("Foreign key:");
    foreignKeyCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    foreignKeyCheckBox.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            foreignKeyCheckBoxActionPerformed(evt);
        }
    });
    checkboxPanel1.add(foreignKeyCheckBox, java.awt.BorderLayout.SOUTH);
    panel.add(checkboxPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 160, 50));
}