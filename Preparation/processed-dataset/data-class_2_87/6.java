public XMLUserData createUserData(String user, String domain, String password) throws CreateUserDataException {
    XMLUserData data;
    String template = parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "userdata.xml";
    File f = new File(template);
    if (!f.exists()) {
        log(Storage.LOG_WARN, "SimpleStorage: User configuration template (" + template + ") doesn't exist!");
        throw new CreateUserDataException("User configuration template (" + template + ") doesn't exist!", user, domain);
    } else if (!f.canRead()) {
        log(Storage.LOG_WARN, "SimpleStorage: User configuration template (" + template + ") is not readable!");
        throw new CreateUserDataException("User configuration template (" + template + ") is not readable!", user, domain);
    }
    Document root;
    try {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        root = parser.parse("file://" + template);
        data = new XMLUserData(root);
        data.init(user, domain, password);
        if (getConfig("SHOW ADVERTISEMENTS").toUpperCase().equals("YES")) {
            if (domain.equals("")) {
                data.setSignature(user + "\n\n" + getConfig("ADVERTISEMENT MESSAGE"));
            } else {
                data.setSignature(user + "@" + domain + "\n\n" + getConfig("ADVERTISEMENT MESSAGE"));
            }
        } else {
            if (domain.equals("")) {
                data.setSignature(user);
            } else {
                data.setSignature(user + "@" + domain);
            }
        }
        data.setTheme(parent.getDefaultTheme());
        WebMailVirtualDomain vdom = getVirtualDomain(domain);
        data.addMailHost("Default", getConfig("DEFAULT PROTOCOL") + "://" + vdom.getDefaultServer(), user, password);
    } catch (Exception ex) {
        log(Storage.LOG_WARN, "SimpleStorage: User configuration template (" + template + ") exists but could not be parsed");
        if (debug)
            ex.printStackTrace();
        throw new CreateUserDataException("User configuration template (" + template + ") exists but could not be parsed", user, domain);
    }
    return data;
}
