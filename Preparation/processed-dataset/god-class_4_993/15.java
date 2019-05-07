/**
     *  Does a check against a known pattern list.
     *
     *  @param context
     *  @param content
     *  @param change
     *  @throws RedirectException
     */
private void checkPatternList(WikiContext context, String content, Change change) throws RedirectException {
    // 
    //  If we have no spam patterns defined, or we're trying to save 
    //  the page containing the patterns, just return. 
    // 
    if (m_spamPatterns == null || context.getPage().getName().equals(m_forbiddenWordsPage)) {
        return;
    }
    String ch = change.toString();
    if (context.getHttpRequest() != null)
        ch += context.getHttpRequest().getRemoteAddr();
    for (Pattern p : m_spamPatterns) {
        // log.debug("Attempting to match page contents with "+p.getPattern()); 
        if (m_matcher.contains(ch, p)) {
            // 
            //  Spam filter has a match. 
            // 
            String uid = log(context, REJECT, REASON_REGEXP + "(" + p.getPattern() + ")", ch);
            log.info("SPAM:Regexp (" + uid + "). Content matches the spam filter '" + p.getPattern() + "'");
            checkStrategy(context, REASON_REGEXP, "Herb says '" + p.getPattern() + "' is a bad spam word and I trust Herb! (Incident code " + uid + ")");
        }
    }
}
