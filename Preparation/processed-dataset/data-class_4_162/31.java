private Element handleAccessRule(String ruleLine) {
    if (m_wysiwygEditorMode) {
        m_currentElement.addContent("[" + ruleLine + "]");
    }
    if (!m_parseAccessRules)
        return m_currentElement;
    Acl acl;
    WikiPage page = m_context.getRealPage();
    // UserDatabase      db = m_context.getEngine().getUserDatabase(); 
    if (ruleLine.startsWith("{"))
        ruleLine = ruleLine.substring(1);
    if (ruleLine.endsWith("}"))
        ruleLine = ruleLine.substring(0, ruleLine.length() - 1);
    if (log.isDebugEnabled())
        log.debug("page=" + page.getName() + ", ACL = " + ruleLine);
    try {
        acl = m_engine.getAclManager().parseAcl(page, ruleLine);
        page.setAcl(acl);
        if (log.isDebugEnabled())
            log.debug(acl.toString());
    } catch (WikiSecurityException wse) {
        return makeError(wse.getMessage());
    }
    return m_currentElement;
}
