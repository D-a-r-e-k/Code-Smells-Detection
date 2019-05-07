/**
     *  Scans startup scripts
     */
private void scanStartupScripts() {
    if (!SPManagerFrame.getParameters().getUseCache())
        SPManagerFrame.getInstance().setRemoteStatusText(SPMTranslate.text("scanningStartupScriptsFrom", new String[] { repository.toString() }), 5000);
    else {
        //String s = repository.toString().replaceAll("/AoIRepository/", "");  
        String s = repository.toString();
        s = s.substring(0, s.lastIndexOf('/'));
        s = s + "/cgi-bin/scripts.cgi?Scripts/Startup%20" + SPManagerPlugin.AOI_VERSION;
        SPManagerFrame.getInstance().setRemoteStatusText(SPMTranslate.text("scanningStartupScriptsFrom", new String[] { s }), 5000);
    }
    if (statusDialog != null)
        statusDialog.setText(SPMTranslate.text("scanningStartupScripts"));
    startupInfo = new Vector();
    if (SPManagerFrame.getParameters().getUseCache()) {
        scanFiles("Scripts/Startup", startupInfo);
    } else {
        try {
            URL startupScriptURL = new URL(repository, "Scripts/Startup/");
            scanFiles(startupScriptURL, startupInfo, ".bsh");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
