//GEN-LAST:event_newMenuItemActionPerformed  
private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_openMenuItemActionPerformed  
    int fileChooserStatus;
    JFileChooser fileChooser = new JFileChooser(getFileChooserStartDir(FILECHOOSER_APPFILE_OPEN));
    ExtensionsFileFilter filter = new ExtensionsFileFilter("xml");
    logToConsole("Opening application file..");
    fileChooser.setDialogTitle("Open an existing application file..");
    fileChooser.setFileFilter(filter);
    fileChooserStatus = fileChooser.showOpenDialog(this);
    if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
        loadApplicationFile(file);
    } else {
        logToConsole("..aborted application file load!");
    }
    if (file != null) {
        fileNameLabel.setText("Application file: " + file.getName());
        fileNameLabel.setToolTipText(file.getAbsolutePath());
        setFileChooserStartDir(FILECHOOSER_APPFILE_OPEN, file);
    }
}
