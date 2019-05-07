public void removed() {
    Server.log(this, "CRITICAL-WARNING: Config file has been removed!\r\nThe Serverconfiguration of the last configuration-file present will stay in charge, but the server won't start if no config is present!", Server.MSG_ERROR, Server.LVL_MAJOR);
}
