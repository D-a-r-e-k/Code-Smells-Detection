private void checkPatternList(WikiContext context, String content, String change) throws RedirectException {
    Change c = new Change();
    c.m_change = change;
    checkPatternList(context, content, c);
}
