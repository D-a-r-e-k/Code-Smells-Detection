/**
   * Load all Mbox properties.
   */
void loadMboxProperties(Properties p) {
    /*
     * set the properties for mbox folders, and for the mbox backend of
     * a pop3 mailbox.  properties set are:
     *
     * mail.mbox.inbox:  the location of the INBOX for this mail store. for
     *   pop3 stores, this is the location of the local copy of the inbox.
     *   for mbox stores, this should be the local inbox file.
     * mail.mbox.userhome:  the location of all subfolders.
     */
    String mailHome = Pooka.getProperty(getStoreProperty() + ".mailDir", "");
    if (mailHome.equals("")) {
        mailHome = Pooka.getProperty("Pooka.defaultMailSubDir", "");
        if (mailHome.equals(""))
            mailHome = Pooka.getPookaManager().getPookaRoot().getAbsolutePath() + java.io.File.separator + ".pooka";
        mailHome = mailHome + java.io.File.separator + storeID;
    }
    mailHome = Pooka.getPookaManager().getResourceManager().translateName(mailHome);
    String inboxFileName;
    if (Pooka.getProperty(getStoreProperty() + ".protocol", "imap").equalsIgnoreCase("pop3")) {
        inboxFileName = mailHome + java.io.File.separator + Pooka.getProperty("Pooka.inboxName", "INBOX");
    } else {
        inboxFileName = Pooka.getProperty(getStoreProperty() + ".inboxLocation", "/var/spool/mail/" + System.getProperty("user.name"));
    }
    String userHomeName = mailHome + java.io.File.separator + Pooka.getProperty("Pooka.subFolderName", "folders");
    getLogger().log(Level.FINE, "for store " + getStoreID() + ", inboxFileName = " + inboxFileName + "; userhome = " + userHomeName);
    p.setProperty("mail.mbox.inbox", inboxFileName);
    p.setProperty("mail.mbox.userhome", userHomeName);
}
