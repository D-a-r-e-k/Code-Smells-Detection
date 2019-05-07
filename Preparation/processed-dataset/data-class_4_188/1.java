/**
   * Loads the column names and sizes.
   */
protected FetchProfile createColumnInformation() {
    FetchProfile fp = super.createColumnInformation();
    // we need to get the full headers for the CachingFolderInfo. 
    fp = new FetchProfile();
    fp.add(FetchProfile.Item.FLAGS);
    fp.add(com.sun.mail.imap.IMAPFolder.FetchProfileItem.HEADERS);
    return fp;
}
