protected void loadXMLSysData() {
    String datapath = parent.getProperty("webmail.data.path");
    String file = "file://" + datapath + System.getProperty("file.separator") + "webmail.xml";
    // String file=datapath+System.getProperty("file.separator")+"webmail.xml"; 
    // bug fixed by Christian Senet 
    Document root;
    try {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        root = parser.parse(file);
        if (debug)
            System.err.println("\nConfiguration file parsed, document: " + root);
        sysdata = new XMLSystemData(root, cs);
        log(Storage.LOG_DEBUG, "SimpleStorage: WebMail configuration loaded.");
    } catch (Exception ex) {
        log(Storage.LOG_ERR, "SimpleStorage: Failed to load WebMail configuration file. Reason: " + ex.getMessage());
        ex.printStackTrace();
        System.exit(0);
    }
}
