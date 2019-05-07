/**
     *  Handles metadata setting [{SET foo=bar}]
     */
private Element handleMetadata(String link) {
    if (m_wysiwygEditorMode) {
        m_currentElement.addContent("[" + link + "]");
    }
    try {
        String args = link.substring(link.indexOf(' '), link.length() - 1);
        String name = args.substring(0, args.indexOf('='));
        String val = args.substring(args.indexOf('=') + 1, args.length());
        name = name.trim();
        val = val.trim();
        if (val.startsWith("'"))
            val = val.substring(1);
        if (val.endsWith("'"))
            val = val.substring(0, val.length() - 1);
        // log.debug("SET name='"+name+"', value='"+val+"'."); 
        if (name.length() > 0 && val.length() > 0) {
            val = m_engine.getVariableManager().expandVariables(m_context, val);
            m_context.getPage().setAttribute(name, val);
        }
    } catch (Exception e) {
        ResourceBundle rb = m_context.getBundle(InternationalizationManager.CORE_BUNDLE);
        Object[] args = { link };
        return makeError(MessageFormat.format(rb.getString("markupparser.error.invalidset"), args));
    }
    return m_currentElement;
}
