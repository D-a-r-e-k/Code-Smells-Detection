/**
     *  Scans plugins
     */
private void scanPlugins() {
    if (!SPManagerFrame.getParameters().getUseCache())
        SPManagerFrame.getInstance().setRemoteStatusText(SPMTranslate.text("scanningPluginsFrom", new String[] { repository.toString() }), 5000);
    else {
        //String s = repository.toString().replaceAll("/AoIRepository/", "");  
        String s = repository.toString();
        s = s.substring(0, s.lastIndexOf('/'));
        s = s + "/cgi-bin/scripts.cgi?Plugins%20" + SPManagerPlugin.AOI_VERSION;
        SPManagerFrame.getInstance().setRemoteStatusText(SPMTranslate.text("scanningPluginsFrom", new String[] { s }), 5000);
    }
    if (statusDialog != null)
        statusDialog.setText(SPMTranslate.text("scanningPlugins"));
    pluginsInfo = new Vector();
    if (SPManagerFrame.getParameters().getUseCache()) {
        scanFiles("Plugins", pluginsInfo);
    } else {
        try {
            URL pluginsURL = new URL(repository, "Plugins/");
            scanFiles(pluginsURL, pluginsInfo, ".jar");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
