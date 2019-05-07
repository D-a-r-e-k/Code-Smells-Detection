/**
   * This loads in the default session properties for this Store's
   * Session.
   */
public Properties loadProperties() {
    Properties p = new Properties(System.getProperties());
    String realProtocol = Pooka.getProperty("Store." + storeID + ".protocol", "");
    if (realProtocol.equalsIgnoreCase("imap")) {
        loadImapProperties(p);
    } else if (realProtocol.equalsIgnoreCase("pop3")) {
        loadPop3Properties(p);
        String useMaildir = Pooka.getProperty(getStoreProperty() + ".useMaildir", "unset");
        if (useMaildir.equals("unset")) {
            Pooka.setProperty(getStoreProperty() + ".useMaildir", "false");
            useMaildir = "false";
        }
        if (useMaildir.equalsIgnoreCase("false")) {
            loadMboxProperties(p);
        } else {
            loadMaildirProperties(p);
        }
    } else if (realProtocol.equalsIgnoreCase("maildir")) {
        loadMaildirProperties(p);
    } else if (realProtocol.equalsIgnoreCase("mbox")) {
        loadMboxProperties(p);
    }
    return p;
}
