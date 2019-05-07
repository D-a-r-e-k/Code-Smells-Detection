private void initDesktop() {
    desktopPane.setLayout(new GridBagLayout());
    this.setTitle("Java Application Generator - Finalist IT Group");
    GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
    gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipadx = 1;
    gridBagConstraints.ipady = 1;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    desktopPane.add(splitPane, gridBagConstraints);
    pack();
}
