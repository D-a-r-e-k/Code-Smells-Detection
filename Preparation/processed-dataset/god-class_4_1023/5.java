/**
   * Load all Maildir properties.
   */
void loadMaildirProperties(Properties p) {
    String mailHome = Pooka.getProperty(getStoreProperty() + ".mailDir", "");
    if (mailHome.equals("")) {
        mailHome = Pooka.getProperty("Pooka.defaultMailSubDir", "");
        if (mailHome.equals(""))
            mailHome = Pooka.getPookaManager().getPookaRoot().getAbsolutePath() + java.io.File.separator + ".pooka";
        mailHome = mailHome + java.io.File.separator + storeID;
    }
    String userHomeName = Pooka.getPookaManager().getResourceManager().translateName(mailHome + java.io.File.separator + Pooka.getProperty("Pooka.subFolderName", "folders"));
    //p.setProperty("mail.store.maildir.imapEmulation", "true"); 
    p.setProperty("mail.store.maildir.baseDir", userHomeName);
    p.setProperty("mail.store.maildir.autocreatedir", "true");
}
