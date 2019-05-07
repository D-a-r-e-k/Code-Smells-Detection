/**
     * Build the GUI.
     */
private void jbInit() throws Exception {
    setIconImage(ACCOUNTS_ICON.getImage());
    navigationTree.setRootVisible(false);
    navigationTree.setCellRenderer(new NavigationTreeCellRenderer());
    newAccountItem.setText(LANGUAGE.getString("MainFrame.newAccount"));
    newAccountItem.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            newAccount();
        }
    });
    deleteAccountItem.setEnabled(false);
    deleteAccountItem.setText(LANGUAGE.getString("MainFrame.deleteAccount"));
    deleteAccountItem.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            deleteAccount();
        }
    });
    navigationTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    navigationTree.setModel(null);
    navigationTree.addTreeSelectionListener(new TreeSelectionListener() {

        public void valueChanged(TreeSelectionEvent e) {
            navigationTreeSelection((DefaultMutableTreeNode) e.getPath().getLastPathComponent());
        }
    });
    // north panel ------------------------------------------------------------- 
    this.getContentPane().add(splitPane, BorderLayout.CENTER);
    splitPane.add(navigationScrollPane, JSplitPane.LEFT);
    splitPane.add(emptyPanel, JSplitPane.RIGHT);
    this.getContentPane().add(toolBar, BorderLayout.NORTH);
    navigationScrollPane.getViewport().add(navigationTree, null);
    navigationPopup.add(newAccountItem);
    navigationPopup.add(deleteAccountItem);
    // this -------------------------------------------------------------------- 
    setTitle(title);
    setJMenuBar(menuBar);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.setAcceptAllFileFilterUsed(true);
    qifFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    qifFileChooser.setMultiSelectionEnabled(false);
    qifFileChooser.setAcceptAllFileFilterUsed(true);
    mt940FileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    mt940FileChooser.setMultiSelectionEnabled(false);
    mt940FileChooser.setAcceptAllFileFilterUsed(true);
    splitPane.setDividerLocation(150);
    navigationTree.addMouseListener(new MouseAdapter() {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                if (navigationTree.getSelectionCount() > 0) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) navigationTree.getSelectionPath().getLastPathComponent();
                    deleteAccountItem.setEnabled(node.getUserObject() instanceof Account);
                }
                navigationPopup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    });
}
