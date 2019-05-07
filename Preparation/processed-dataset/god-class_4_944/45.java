//GEN-LAST:event_driverManagerMenuItemActionPerformed  
private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_exportMenuItemActionPerformed  
    if (file == null) {
        logger.log("UML Export: save application file first!");
        String message = "Before exporting the current application to UML, please save it to an application file.";
        JOptionPane.showMessageDialog(this, message, "No application file!", JOptionPane.ERROR_MESSAGE);
        saveButtonActionPerformed(null);
        if (file == null) {
            logger.log("Aborted UML Export!");
            return;
        }
    } else {
        saveButtonActionPerformed(null);
    }
    int fileChooserStatus;
    logToConsole("Exporting application to an XMI file.  Please wait...");
    final JFileChooser fileChooser = new JFileChooser(getFileChooserStartDir(FILECHOOSER_UMLEXPORT));
    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
    String[] extenstions = { "xmi", "xml" };
    ExtensionsFileFilter filter = new ExtensionsFileFilter(extenstions);
    fileChooser.setFileFilter(filter);
    fileChooser.setDialogTitle("UML Export: Choose a destination XMI file..");
    fileChooserStatus = fileChooser.showSaveDialog(this);
    if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
        File xmiFile = fileChooser.getSelectedFile();
        if (!xmiFile.getAbsolutePath().endsWith(XMI_SUFFIX)) {
            xmiFile = new File(xmiFile.getAbsolutePath() + XMI_SUFFIX);
        }
        //run the export tool  
        new Jag2UMLGenerator(logger).generateXMI(file.getAbsolutePath(), xmiFile);
        logToConsole("...UML export complete.");
        setFileChooserStartDir(FILECHOOSER_UMLEXPORT, xmiFile);
    } else {
        logToConsole("...aborted!");
    }
}
