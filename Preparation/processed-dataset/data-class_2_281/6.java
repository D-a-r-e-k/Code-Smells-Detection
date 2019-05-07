/**
     *  Scans objects scripts
     */
private void scanObjectScripts() {
    if (!SPManagerFrame.getParameters().getUseCache())
        SPManagerFrame.getInstance().setRemoteStatusText(SPMTranslate.text("scanningObjectScriptsFrom", new String[] { repository.toString() }), 5000);
    else {
        //String s = repository.toString().replaceAll("/AoIRepository/", "");  
        String s = repository.toString();
        s = s.substring(0, s.lastIndexOf('/'));
        s = s + "/cgi-bin/scripts.cgi?Scripts/Objects%20" + SPManagerPlugin.AOI_VERSION;
        SPManagerFrame.getInstance().setRemoteStatusText(SPMTranslate.text("scanningObjectScriptsFrom", new String[] { s }), 5000);
    }
    if (statusDialog != null)
        statusDialog.setText(SPMTranslate.text("scanningObjectScripts"));
    objectInfo = new Vector();
    if (SPManagerFrame.getParameters().getUseCache()) {
        scanFiles("Scripts/Objects", objectInfo);
    } else {
        try {
            URL objectScriptURL = new URL(repository, "Scripts/Objects/");
            scanFiles(objectScriptURL, objectInfo, ".bsh");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
