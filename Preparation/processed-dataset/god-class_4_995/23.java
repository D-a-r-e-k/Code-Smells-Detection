/**
     *  Does a lazy init.  Otherwise, we would get into a situation
     *  where HTMLRenderer would try and boot a TranslatorReader before
     *  the TranslatorReader it is contained by is up.
     */
private JSPWikiMarkupParser getCleanTranslator() {
    if (m_cleanTranslator == null) {
        WikiContext dummyContext = new WikiContext(m_engine, m_context.getHttpRequest(), m_context.getPage());
        m_cleanTranslator = new JSPWikiMarkupParser(dummyContext, null);
        m_cleanTranslator.m_allowHTML = true;
    }
    return m_cleanTranslator;
}
