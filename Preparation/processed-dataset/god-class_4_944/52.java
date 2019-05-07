//GEN-LAST:event_addRelationPopupMenuItemActionPerformed  
private void contentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //GEN-FIRST:event_contentMenuItemActionPerformed  
    URL helpURL = null;
    String s = null;
    try {
        s = "file:" + System.getProperty("user.dir") + System.getProperty("file.separator") + "../doc/help/help.html";
        helpURL = new URL(s);
    } catch (IOException e) {
        JagGenerator.logToConsole("Missing help file: " + s, LogLevel.ERROR);
    }
    new HtmlContentPopUp(null, "JAG Help", false, helpURL).show();
}
