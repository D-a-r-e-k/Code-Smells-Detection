public void setName(String name) throws Exception {
    if (getName() == null) {
        // if creating new folder 
        super.setName(name);
        return;
    }
    String oldPath = getImapPath();
    LOG.info("old path=" + oldPath);
    String newPath = null;
    if (getParent() instanceof IMAPFolder) {
        newPath = ((IMAPFolder) getParent()).getImapPath();
    }
    newPath += (getServer().getDelimiter() + name);
    LOG.info("new path=" + newPath);
    getServer().renameFolder(oldPath, newPath);
    super.setName(name);
}
