/**
     * Install an application for the specified path from the specified
     * web application archive.
     *
     * @param writer Writer to render results to
     * @param config URL of the context configuration file to be installed
     * @param path Context path of the application to be installed
     * @param war URL of the web application archive to be installed
     * @param update true to override any existing webapp on the path
     */
protected void deploy(PrintWriter writer, String config, String path, String war, boolean update) {
    if (config != null && config.length() == 0) {
        config = null;
    }
    if (war != null && war.length() == 0) {
        war = null;
    }
    if (debug >= 1) {
        if (config != null && config.length() > 0) {
            if (war != null) {
                log("install: Installing context configuration at '" + config + "' from '" + war + "'");
            } else {
                log("install: Installing context configuration at '" + config + "'");
            }
        } else {
            if (path != null && path.length() > 0) {
                log("install: Installing web application at '" + path + "' from '" + war + "'");
            } else {
                log("install: Installing web application from '" + war + "'");
            }
        }
    }
    if (path == null || path.length() == 0 || !path.startsWith("/")) {
        writer.println(sm.getString("managerServlet.invalidPath", RequestUtil.filter(path)));
        return;
    }
    String displayPath = path;
    if ("/".equals(path)) {
        path = "";
    }
    // Check if app already exists, or undeploy it if updating 
    Context context = (Context) host.findChild(path);
    if (update) {
        if (context != null) {
            undeploy(writer, displayPath);
        }
        context = (Context) host.findChild(path);
    }
    if (context != null) {
        writer.println(sm.getString("managerServlet.alreadyContext", displayPath));
        return;
    }
    if (config != null && (config.startsWith("file:"))) {
        config = config.substring("file:".length());
    }
    if (war != null && (war.startsWith("file:"))) {
        war = war.substring("file:".length());
    }
    try {
        if (!isServiced(path)) {
            addServiced(path);
            try {
                if (config != null) {
                    configBase.mkdirs();
                    copy(new File(config), new File(configBase, getConfigFile(path) + ".xml"));
                }
                if (war != null) {
                    if (war.endsWith(".war")) {
                        copy(new File(war), new File(getAppBase(), getDocBase(path) + ".war"));
                    } else {
                        copy(new File(war), new File(getAppBase(), getDocBase(path)));
                    }
                }
                // Perform new deployment 
                check(path);
            } finally {
                removeServiced(path);
            }
        }
        context = (Context) host.findChild(path);
        if (context != null && context.getConfigured() && context.getAvailable()) {
            writer.println(sm.getString("managerServlet.deployed", displayPath));
        } else if (context != null && !context.getAvailable()) {
            writer.println(sm.getString("managerServlet.deployedButNotStarted", displayPath));
        } else {
            // Something failed 
            writer.println(sm.getString("managerServlet.deployFailed", displayPath));
        }
    } catch (Throwable t) {
        log("ManagerServlet.install[" + displayPath + "]", t);
        writer.println(sm.getString("managerServlet.exception", t.toString()));
    }
}
