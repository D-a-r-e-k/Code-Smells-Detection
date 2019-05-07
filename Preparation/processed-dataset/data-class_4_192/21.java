/**
   *
   */
private void doOpenFolders(FolderInfo fi) {
    if (Pooka.getProperty("Pooka.openFoldersInBackground", "false").equalsIgnoreCase("true")) {
        final FolderInfo current = fi;
        javax.swing.AbstractAction openFoldersAction = new javax.swing.AbstractAction() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                current.openAllFolders(Folder.READ_WRITE);
            }
        };
        openFoldersAction.putValue(javax.swing.Action.NAME, "file-open");
        openFoldersAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "file-open on folder " + fi.getFolderID());
        getStoreThread().addToQueue(openFoldersAction, new java.awt.event.ActionEvent(this, 0, "open-all"), ActionThread.PRIORITY_LOW);
    } else {
        fi.openAllFolders(Folder.READ_WRITE);
    }
}
