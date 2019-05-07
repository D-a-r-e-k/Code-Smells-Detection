public void changed() {
    try {
        FileInputStream fis = new FileInputStream(configFile);
        Properties tprop = new Properties();
        tprop.load(fis);
        fis.close();
        props = tprop;
        checkForConfigValues();
        lastModified = configFile.lastModified();
        Server.log(this, "reload: reloaded configfile", Server.MSG_STATE, Server.LVL_MINOR);
    } catch (Exception e) {
        Server.debug(this, "reload: ", e, Server.MSG_ERROR, Server.LVL_MAJOR);
    }
}
