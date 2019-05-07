private void checkStrategy(WikiContext context, String error, String message) throws RedirectException {
    if (m_stopAtFirstMatch) {
        throw new RedirectException(message, getRedirectPage(context));
    }
    Integer score = (Integer) context.getVariable(ATTR_SPAMFILTER_SCORE);
    if (score != null)
        score = score + 1;
    else
        score = 1;
    context.setVariable(ATTR_SPAMFILTER_SCORE, score);
}
