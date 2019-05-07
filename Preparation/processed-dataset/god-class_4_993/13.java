/**
     *  Checks the ban list if the IP address of the changer is already on it.
     *
     *  @param context
     *  @throws RedirectException
     */
private void checkBanList(WikiContext context, Change change) throws RedirectException {
    HttpServletRequest req = context.getHttpRequest();
    if (req != null) {
        String remote = req.getRemoteAddr();
        long now = System.currentTimeMillis();
        for (Iterator i = m_temporaryBanList.iterator(); i.hasNext(); ) {
            Host host = (Host) i.next();
            if (host.getAddress().equals(remote)) {
                long timeleft = (host.getReleaseTime() - now) / 1000L;
                log(context, REJECT, REASON_IP_BANNED_TEMPORARILY, change.m_change);
                checkStrategy(context, REASON_IP_BANNED_TEMPORARILY, "You have been temporarily banned from modifying this wiki. (" + timeleft + " seconds of ban left)");
            }
        }
    }
}
