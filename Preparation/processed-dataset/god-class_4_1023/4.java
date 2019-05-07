/**
   * Load all POP3 properties.
   */
void loadPop3Properties(Properties p) {
    if (Pooka.getProperty(getStoreProperty() + ".SSL", "false").equalsIgnoreCase("true")) {
        //p.setProperty("mail.pop3s.socketFactory.class", "net.suberic.pooka.ssl.PookaSSLSocketFactory"); 
        p.setProperty("mail.pop3s.socketFactory.fallback", Pooka.getProperty(getStoreProperty() + ".SSL.fallback", "false"));
    }
}
