/**
     * @see net.wastl.webmail.server.Storage.getUserData()
     *
     * devink 7/15/2000 - Added TwoPassAuthenticationException
     *                  - changed to doAuth*UserData()
     *                  - Added challenged arg.
     * 9/24/2000        - reverted to old getUserData for new cr auth 
     */
public XMLUserData getUserData(String user, String domain, String password, boolean authenticate) throws UserDataException, InvalidPasswordException {
    if (authenticate) {
        auth.authenticatePreUserData(user, domain, password);
    }
    if (user.equals("")) {
        return null;
    }
    XMLUserData data = (XMLUserData) user_cache.get(user + user_domain_separator + domain);
    if (data == null) {
        user_cache.miss();
        String filename = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain + System.getProperty("file.separator") + user + ".xml";
        boolean error = true;
        File f = new File(filename);
        if (f.exists() && f.canRead()) {
            log(Storage.LOG_INFO, "SimpleStorage: Reading user configuration (" + f.getAbsolutePath() + ") for " + user);
            long t_start = System.currentTimeMillis();
            try {
                DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document root = parser.parse(new InputSource(new InputStreamReader(new FileInputStream(filename), "UTF-8")));
                // 		    Document root = parser.parse(new InputSource(new InputStreamReader(new FileInputStream(f.getAbsolutePath()), "UTF-8"))); 
                data = new XMLUserData(root);
                if (debug)
                    System.err.println("SimpleStorage: Parsed Document " + root);
                error = false;
            } catch (Exception ex) {
                log(Storage.LOG_WARN, "SimpleStorage: User configuration for " + user + " exists but could not be parsed (" + ex.getMessage() + ")");
                if (debug)
                    ex.printStackTrace();
                error = true;
            }
            long t_end = System.currentTimeMillis();
            log(Storage.LOG_DEBUG, "SimpleStorage: Parsing of XML userdata for " + user + ", domain " + domain + " took " + (t_end - t_start) + "ms.");
            if (authenticate) {
                auth.authenticatePostUserData(data, domain, password);
            }
        }
        if (error && !f.exists()) {
            log(Storage.LOG_INFO, "UserConfig: Creating user configuration for " + user);
            data = createUserData(user, domain, password);
            error = false;
            if (authenticate) {
                auth.authenticatePostUserData(data, domain, password);
            }
        }
        if (error) {
            log(Storage.LOG_ERR, "UserConfig: Could not read userdata for " + user + "!");
            throw new UserDataException("Could not read userdata!", user, domain);
        }
        user_cache.put(user + user_domain_separator + domain, data);
    } else {
        user_cache.hit();
        if (authenticate) {
            auth.authenticatePostUserData(data, domain, password);
        }
    }
    return data;
}
