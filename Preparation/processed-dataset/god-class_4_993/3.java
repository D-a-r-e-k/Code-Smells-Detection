/** {@inheritDoc} */
public String preSave(WikiContext context, String content) throws RedirectException {
    cleanBanList();
    refreshBlacklists(context);
    Change change = getChange(context, content);
    if (!ignoreThisUser(context)) {
        checkBanList(context, change);
        checkSinglePageChange(context, content, change);
        checkPatternList(context, content, change);
    }
    if (!m_stopAtFirstMatch) {
        Integer score = (Integer) context.getVariable(ATTR_SPAMFILTER_SCORE);
        if (score != null && score.intValue() >= m_scoreLimit) {
            throw new RedirectException("Herb says you got too many points", getRedirectPage(context));
        }
    }
    log(context, ACCEPT, "-", change.toString());
    return content;
}
