/**
     *  Checks against the akismet system.
     *
     * @param context
     * @param change
     * @throws RedirectException
     */
private void checkAkismet(WikiContext context, Change change) throws RedirectException {
    if (m_akismetAPIKey != null) {
        if (m_akismet == null) {
            log.info("Initializing Akismet spam protection.");
            m_akismet = new Akismet(m_akismetAPIKey, context.getEngine().getBaseURL());
            if (!m_akismet.verifyAPIKey()) {
                log.error("Akismet API key cannot be verified.  Please check your config.");
                m_akismetAPIKey = null;
                m_akismet = null;
            }
        }
        HttpServletRequest req = context.getHttpRequest();
        // 
        //  Akismet will mark all empty statements as spam, so we'll just 
        //  ignore them. 
        // 
        if (change.m_adds == 0 && change.m_removals > 0) {
            return;
        }
        if (req != null && m_akismet != null) {
            log.debug("Calling Akismet to check for spam...");
            StopWatch sw = new StopWatch();
            sw.start();
            String ipAddress = req.getRemoteAddr();
            String userAgent = req.getHeader("User-Agent");
            String referrer = req.getHeader("Referer");
            String permalink = context.getViewURL(context.getPage().getName());
            String commentType = context.getRequestContext().equals(WikiContext.COMMENT) ? "comment" : "edit";
            String commentAuthor = context.getCurrentUser().getName();
            String commentAuthorEmail = null;
            String commentAuthorURL = null;
            boolean isSpam = m_akismet.commentCheck(ipAddress, userAgent, referrer, permalink, commentType, commentAuthor, commentAuthorEmail, commentAuthorURL, change.toString(), null);
            sw.stop();
            log.debug("Akismet request done in: " + sw);
            if (isSpam) {
                // Host host = new Host( ipAddress, null ); 
                // m_temporaryBanList.add( host ); 
                String uid = log(context, REJECT, REASON_AKISMET, change.toString());
                log.info("SPAM:Akismet (" + uid + "). Akismet thinks this change is spam; added host to temporary ban list.");
                checkStrategy(context, REASON_AKISMET, "Akismet tells Herb you're a spammer, Herb trusts Akismet, and I trust Herb! (Incident code " + uid + ")");
            }
        }
    }
}
