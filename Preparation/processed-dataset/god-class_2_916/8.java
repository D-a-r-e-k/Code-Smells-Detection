public void saveUserData(String user, String domain) {
    try {
        String path = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain;
        File p = new File(path);
        if ((p.exists() && p.isDirectory()) || p.mkdirs()) {
            File f = new File(path + System.getProperty("file.separator") + user + ".xml");
            if ((!f.exists() && p.canWrite()) || f.canWrite()) {
                XMLUserData userdata = getUserData(user, domain, "", false);
                Document d = userdata.getRoot();
                long t_start = System.currentTimeMillis();
                FileOutputStream out = new FileOutputStream(f);
                // 		    XMLCommon.writeXML(d,out,parent.getProperty("webmail.xml.path")+ 
                // 				       System.getProperty("file.separator")+"userdata.dtd"); 
                XMLCommon.writeXML(d, out, "file://" + parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "userdata.dtd");
                out.flush();
                out.close();
                long t_end = System.currentTimeMillis();
                log(Storage.LOG_DEBUG, "SimpleStorage: Serializing userdata for " + user + ", domain " + domain + " took " + (t_end - t_start) + "ms.");
            } else {
                log(Storage.LOG_WARN, "SimpleStorage: Could not write userdata (" + f.getAbsolutePath() + ") for user " + user);
            }
        } else {
            log(Storage.LOG_ERR, "SimpleStorage: Could not create path " + path + ". Aborting with user " + user);
        }
    } catch (Exception ex) {
        log(Storage.LOG_ERR, "SimpleStorage: Unexpected error while trying to save user configuration " + "for user " + user + "(" + ex.getMessage() + ").");
        if (debug)
            ex.printStackTrace();
    }
}
