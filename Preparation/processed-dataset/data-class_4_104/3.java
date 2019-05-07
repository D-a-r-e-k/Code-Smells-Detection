public void readConfig() {
    Server.log(this, "FreeCS Startup", MSG_CONFIG, LVL_MINOR);
    StringBuffer sb = new StringBuffer(BASE_PATH).append("/config");
    File cFile = new File(sb.toString());
    if (!cFile.exists()) {
        Server.log(this, "config directory missing\r\n" + BASE_PATH + "/config", MSG_ERROR, LVL_HALT);
    }
    sb = new StringBuffer(BASE_PATH).append("/config/config.cfg");
    cFile = new File(sb.toString());
    if (!cFile.exists()) {
        Server.log(this, "config file missing\r\n" + sb.toString(), MSG_ERROR, LVL_HALT);
    }
    try {
        FileInputStream in = new FileInputStream(cFile);
        props.load(in);
        in.close();
    } catch (FileNotFoundException fnfe) {
    } catch (IOException ioe) {
        Server.log(this, "unable to read config-files", MSG_ERROR, LVL_HALT);
    }
    // check for port-specification  
    if (props.getProperty("port") == null)
        Server.log(this, "No port specified in config: port=[portnumber]", MSG_ERROR, LVL_HALT);
    checkForConfigValues();
    configFile = cFile;
    lastModified = cFile.lastModified();
    FileMonitor.getFileMonitor().addReloadable(srv);
}
