//GEN-LAST:event_openMenuItemActionPerformed  
public void loadApplicationFile(File file) {
    this.file = file;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    Document doc = null;
    try {
        builder = dbf.newDocumentBuilder();
        doc = builder.parse(file);
        root = new Root(doc);
        logToConsole("..application file " + file + " loaded!");
        treeModel.setRoot(root);
        tree.setSelectionPath(new TreePath(((DefaultMutableTreeNode) root.getFirstChild()).getPath()));
        setFileNeedsSavingIndicator(false);
        SelectTablesDialog.clear();
        disconnectMenuItemActionPerformed(null);
        getRecentMenu().addToRecentList(file.getAbsolutePath());
    } catch (Exception e) {
        e.printStackTrace();
        logToConsole("Failed to load application file! (" + e + ")", LogLevel.ERROR);
        getRecentMenu().removeFromRecentList(file.getAbsolutePath());
    }
}
