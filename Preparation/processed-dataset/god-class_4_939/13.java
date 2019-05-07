public void initServer() {
    try {
        if (templatemanager == null)
            templatemanager = new TemplateManager();
    } catch (IOException ioe) {
        Server.debug(this, "unable to load TemplateSet: ", ioe, MSG_ERROR, LVL_HALT);
    }
    auth = AuthManager.instance;
    auth.init();
    Runtime rt = Runtime.getRuntime();
    // register the CleanupClass as shutdown-hook  
    rt.addShutdownHook(new CleanupClass());
}
