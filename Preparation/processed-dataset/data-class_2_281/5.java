/**
     *  Scans tools scripts
     */
private void scanToolScripts() {
    if (!SPManagerFrame.getParameters().getUseCache())
        SPManagerFrame.getInstance().setRemoteStatusText(SPMTranslate.text("scanningToolScriptsFrom", new String[] { repository.toString() }), 5000);
    else {
        //String s = repository.toString().replaceAll("/AoIRepository/", "");  
        String s = repository.toString();
        s = s.substring(0, s.lastIndexOf('/'));
        s = s + "/cgi-bin/scripts.cgi?Scripts/Tools%20" + SPManagerPlugin.AOI_VERSION;
        SPManagerFrame.getInstance().setRemoteStatusText(SPMTranslate.text("scanningToolScriptsFrom", new String[] { s }), 5000);
    }
    if (statusDialog != null)
        statusDialog.setText(SPMTranslate.text("scanningToolScripts"));
    toolInfo = new Vector();
    if (SPManagerFrame.getParameters().getUseCache()) {
        scanFiles("Scripts/Tools", toolInfo);
    } else {
        try {
            URL toolScriptURL = new URL(repository, "Scripts/Tools/");
            scanFiles(toolScriptURL, toolInfo, ".bsh");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
