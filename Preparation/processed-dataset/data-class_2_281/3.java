/**
     *  Init stuff
     */
public void initialize() {
    super.initialize();
    statusDialog = null;
    scanPlugins();
    if (!unknownHost)
        scanToolScripts();
    if (!unknownHost)
        scanObjectScripts();
    if (!unknownHost)
        scanStartupScripts();
    initialized = true;
}
