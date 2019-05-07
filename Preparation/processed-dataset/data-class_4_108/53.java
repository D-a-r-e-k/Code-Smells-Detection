//GEN-LAST:event_contentMenuItemActionPerformed  
private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_aboutMenuItemActionPerformed  
    URL helpURL = null;
    String s = null;
    try {
        s = "file:" + System.getProperty("user.dir") + System.getProperty("file.separator") + "../doc/help/about.html";
        helpURL = new URL(s);
    } catch (IOException e) {
        JagGenerator.logToConsole("Missing help file: " + s, LogLevel.ERROR);
    }
    new HtmlContentPopUp(null, "JAG About", false, helpURL).show();
}
