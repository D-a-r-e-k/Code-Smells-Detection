protected void saveXMLSysData() {
    try {
        Document d = sysdata.getRoot();
        OutputStream cfg_out = new FileOutputStream(parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + "webmail.xml");
        XMLCommon.writeXML(d, cfg_out, "file://" + parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "sysdata.dtd");
        // 	    XMLCommon.writeXML(d,cfg_out,parent.getProperty("webmail.xml.path")+ 
        // 			       System.getProperty("file.separator")+"sysdata.dtd"); 
        cfg_out.flush();
        cfg_out.close();
        sysdata.setLoadTime(System.currentTimeMillis());
        log(Storage.LOG_DEBUG, "SimpleStorage: WebMail configuration saved.");
    } catch (Exception ex) {
        log(Storage.LOG_ERR, "SimpleStorage: Error while trying to save WebMail configuration (" + ex.getMessage() + ").");
        ex.printStackTrace();
    }
}
