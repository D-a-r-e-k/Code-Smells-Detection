/**
     *  Returns link name, if it exists; otherwise it returns null.
     */
private String linkExists(String page) {
    try {
        if (page == null || page.length() == 0)
            return null;
        return m_engine.getFinalPageName(page);
    } catch (ProviderException e) {
        log.warn("TranslatorReader got a faulty page name!", e);
        return page;
    }
}
