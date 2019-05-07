//GEN-LAST:event_aboutMenuItemActionPerformed  
private void generateJavaApplicationAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_generateJavaApplicationAsMenuItemActionPerformed  
    if (evt.getActionCommand() == STOP_ACTION) {
        runningThread.interrupt();
        return;
    }
    if (file == null) {
        logger.log("No file specified! Save file first.");
        String message = "No application file (XML skelet) has been selected.\n" + "Please save the current application to a file or open an existing application file.";
        JOptionPane.showMessageDialog(this, message, "No application file!", JOptionPane.ERROR_MESSAGE);
    } else {
        SkeletValidator validator = new SkeletValidator(root, tree, entitiesByTableName, logger);
        String message = validator.validateSkelet();
        if (message != null) {
            logger.log("Not a valid application file!");
            message += "\r\nSelect 'Yes' if you want to generate anyway. This will very probably lead to incorrect code!";
            //JOptionPane.showMessageDialog(this, message, "Invalid configuration", JOptionPane.YES_NO_OPTION);  
            int rc = JOptionPane.showConfirmDialog(this, message, "Invalid configuration", JOptionPane.YES_NO_OPTION);
            if (rc != 0) {
                // 0 is the yes option, which means we want to generate anyway.  
                return;
            }
            logger.log("Warning! Code is generated in spite of an invalid project file!");
        }
        // Make sure the lates skelet has been saved:  
        if (!save()) {
            logger.log("Can't generate application - Invalid relation(s).");
            return;
        }
        String outDir = Settings.getLastSelectedOutputDir();
        // Now select an output directory for the generated java application.  
        if (outputDir != null) {
            outDir = outputDir.getParentFile().getAbsolutePath();
        }
        outputDir = selectJagOutDirectory(outDir);
        if (outputDir == null) {
            return;
        }
        Settings.setLastSelectedOutputDir(outputDir.getParentFile().getAbsolutePath());
        final String[] args = new String[3];
        args[0] = outputDir.getAbsolutePath();
        args[1] = file.getAbsolutePath();
        runningThread = new Thread() {

            public void run() {
                logger.log("Running jag in the " + args[0] + " directory for application file: " + args[1]);
                JApplicationGen.setLogger(logger);
                JApplicationGen.main(args);
            }
        };
        runningThread.start();
        executeButton.setIcon(CANCEL_ICON);
        executeButton.setActionCommand(STOP_ACTION);
    }
    setFileNeedsSavingIndicator(false);
}
