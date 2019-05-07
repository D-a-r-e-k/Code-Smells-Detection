/**
     *  Goes through the ban list and cleans away any host which has expired from it.
     */
private synchronized void cleanBanList() {
    long now = System.currentTimeMillis();
    for (Iterator i = m_temporaryBanList.iterator(); i.hasNext(); ) {
        Host host = (Host) i.next();
        if (host.getReleaseTime() < now) {
            log.debug("Removed host " + host.getAddress() + " from temporary ban list (expired)");
            i.remove();
        }
    }
}
