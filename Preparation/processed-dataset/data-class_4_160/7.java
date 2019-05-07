/**
     *  Takes a single page change and performs a load of tests on the content change.
     *  An admin can modify anything.
     *
     *  @param context
     *  @param content
     *  @throws RedirectException
     */
private synchronized void checkSinglePageChange(WikiContext context, String content, Change change) throws RedirectException {
    HttpServletRequest req = context.getHttpRequest();
    if (req != null) {
        String addr = req.getRemoteAddr();
        int hostCounter = 0;
        int changeCounter = 0;
        log.debug("Change is " + change.m_change);
        long time = System.currentTimeMillis() - 60 * 1000L;
        // 1 minute 
        for (Iterator i = m_lastModifications.iterator(); i.hasNext(); ) {
            Host host = (Host) i.next();
            // 
            //  Check if this item is invalid 
            // 
            if (host.getAddedTime() < time) {
                log.debug("Removed host " + host.getAddress() + " from modification queue (expired)");
                i.remove();
                continue;
            }
            // 
            // Check if this IP address has been seen before 
            // 
            if (host.getAddress().equals(addr)) {
                hostCounter++;
            }
            // 
            //  Check, if this change has been seen before 
            // 
            if (host.getChange() != null && host.getChange().equals(change)) {
                changeCounter++;
            }
        }
        // 
        //  Now, let's check against the limits. 
        // 
        if (hostCounter >= m_limitSinglePageChanges) {
            Host host = new Host(addr, null);
            m_temporaryBanList.add(host);
            String uid = log(context, REJECT, REASON_TOO_MANY_MODIFICATIONS, change.m_change);
            log.info("SPAM:TooManyModifications (" + uid + "). Added host " + addr + " to temporary ban list for doing too many modifications/minute");
            checkStrategy(context, REASON_TOO_MANY_MODIFICATIONS, "Herb says you look like a spammer, and I trust Herb! (Incident code " + uid + ")");
        }
        if (changeCounter >= m_limitSimilarChanges) {
            Host host = new Host(addr, null);
            m_temporaryBanList.add(host);
            String uid = log(context, REJECT, REASON_SIMILAR_MODIFICATIONS, change.m_change);
            log.info("SPAM:SimilarModifications (" + uid + "). Added host " + addr + " to temporary ban list for doing too many similar modifications");
            checkStrategy(context, REASON_SIMILAR_MODIFICATIONS, "Herb says you look like a spammer, and I trust Herb! (Incident code " + uid + ")");
        }
        // 
        //  Calculate the number of links in the addition. 
        // 
        String tstChange = change.toString();
        int urlCounter = 0;
        while (m_matcher.contains(tstChange, m_urlPattern)) {
            MatchResult m = m_matcher.getMatch();
            tstChange = tstChange.substring(m.endOffset(0));
            urlCounter++;
        }
        if (urlCounter > m_maxUrls) {
            Host host = new Host(addr, null);
            m_temporaryBanList.add(host);
            String uid = log(context, REJECT, REASON_TOO_MANY_URLS, change.toString());
            log.info("SPAM:TooManyUrls (" + uid + "). Added host " + addr + " to temporary ban list for adding too many URLs");
            checkStrategy(context, REASON_TOO_MANY_URLS, "Herb says you look like a spammer, and I trust Herb! (Incident code " + uid + ")");
        }
        // 
        //  Check bot trap 
        // 
        checkBotTrap(context, change);
        // 
        //  Check UTF-8 mangling 
        // 
        checkUTF8(context, change);
        // 
        //  Do Akismet check.  This is good to be the last, because this is the most 
        //  expensive operation. 
        // 
        checkAkismet(context, change);
        m_lastModifications.add(new Host(addr, change));
    }
}
