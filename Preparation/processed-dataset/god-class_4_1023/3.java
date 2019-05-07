/**
   * Load all IMAP properties.
   */
void loadImapProperties(Properties p) {
    p.setProperty("mail.imap.timeout", Pooka.getProperty(getStoreProperty() + ".timeout", Pooka.getProperty("Pooka.timeout", "-1")));
    p.setProperty("mail.imap.connectiontimeout", Pooka.getProperty(getStoreProperty() + ".connectionTimeout", Pooka.getProperty("Pooka.connectionTimeout", "-1")));
    p.setProperty("mail.imaps.timeout", Pooka.getProperty(getStoreProperty() + ".timeout", Pooka.getProperty("Pooka.timeout", "-1")));
    p.setProperty("mail.imaps.connectiontimeout", Pooka.getProperty(getStoreProperty() + ".connectionTimeout", Pooka.getProperty("Pooka.connectionTimeout", "-1")));
    // set up ssl 
    if (sslSetting.equals("ssl")) {
        p.setProperty("mail.imaps.socketFactory.fallback", Pooka.getProperty(getStoreProperty() + ".SSL.fallback", "false"));
    } else if (sslSetting.equals("tlsrequired")) {
        p.setProperty("mail.imap.starttls.enable", "true");
    } else if (sslSetting.equals("tls")) {
        // failover is implemented in the connectStore() method. 
        p.setProperty("mail.imap.starttls.enable", "true");
    }
    // use a dedicated store connection. 
    p.setProperty("mail.imap.separatestoreconnection", "true");
    p.setProperty("mail.imaps.separatestoreconnection", "true");
}
