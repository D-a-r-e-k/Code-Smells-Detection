//GEN-LAST:event_saveMenuItemActionPerformed  
private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_saveAsMenuItemActionPerformed  
    int fileChooserStatus;
    JFileChooser fileChooser = new JFileChooser(getFileChooserStartDir(FILECHOOSER_APPFILE_SAVE));
    fileChooser.setDialogTitle("Save application file..");
    ExtensionsFileFilter filter = new ExtensionsFileFilter("xml");
    fileChooser.setFileFilter(filter);
    fileChooserStatus = fileChooser.showSaveDialog(this);
    if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
        setFileChooserStartDir(FILECHOOSER_APPFILE_SAVE, file);
        save();
    }
}
