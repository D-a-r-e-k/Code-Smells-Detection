//GEN-LAST:event_exportMenuItemActionPerformed  
private void importMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_importMenuItemActionPerformed  
    int fileChooserStatus;
    logToConsole("Importing UML model from XMI file.  Please wait...");
    final JFileChooser fileChooser = new JFileChooser(getFileChooserStartDir(FILECHOOSER_UMLIMPORT));
    String[] extenstions = { "xmi", "xml" };
    ExtensionsFileFilter filter = new ExtensionsFileFilter(extenstions);
    fileChooser.setDialogTitle("UML Import: Choose an XMI file..");
    fileChooser.setFileFilter(filter);
    fileChooserStatus = fileChooser.showOpenDialog(this);
    if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
        String xmiFile = fileChooser.getSelectedFile().getAbsolutePath();
        String outputDir = ".";
        //run the import tool - creates an XML application file in the output directory  
        File xmi = new UML2JagGenerator(logger).generateXML(xmiFile, outputDir);
        log.info("Generated the jag project file from the UML Model. Now load the file to JAG.");
        loadApplicationFile(xmi);
        log.info("JAG project file was loaded.");
        xmi.delete();
        // delete the generated XML file: give the user the choice of where to store it later.  
        logToConsole("...UML import complete.");
        setFileChooserStartDir(FILECHOOSER_UMLIMPORT, fileChooser.getSelectedFile());
    } else {
        logToConsole("...aborted!");
    }
}
