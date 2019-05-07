/**
     *  {@inheritDoc}
     */
@Override
public void initialize(WikiEngine engine, Properties properties) {
    m_forbiddenWordsPage = properties.getProperty(PROP_WORDLIST, m_forbiddenWordsPage);
    m_errorPage = properties.getProperty(PROP_ERRORPAGE, m_errorPage);
    m_limitSinglePageChanges = TextUtil.getIntegerProperty(properties, PROP_PAGECHANGES, m_limitSinglePageChanges);
    m_limitSimilarChanges = TextUtil.getIntegerProperty(properties, PROP_SIMILARCHANGES, m_limitSimilarChanges);
    m_maxUrls = TextUtil.getIntegerProperty(properties, PROP_MAXURLS, m_maxUrls);
    m_banTime = TextUtil.getIntegerProperty(properties, PROP_BANTIME, m_banTime);
    m_blacklist = properties.getProperty(PROP_BLACKLIST, m_blacklist);
    m_ignoreAuthenticated = TextUtil.getBooleanProperty(properties, PROP_IGNORE_AUTHENTICATED, m_ignoreAuthenticated);
    m_useCaptcha = properties.getProperty(PROP_CAPTCHA, "").equals("asirra");
    try {
        m_urlPattern = m_compiler.compile(URL_REGEXP);
    } catch (MalformedPatternException e) {
        log.fatal("Internal error: Someone put in a faulty pattern.", e);
        throw new InternalWikiException("Faulty pattern.");
    }
    m_akismetAPIKey = TextUtil.getStringProperty(properties, PROP_AKISMET_API_KEY, m_akismetAPIKey);
    m_stopAtFirstMatch = TextUtil.getStringProperty(properties, PROP_FILTERSTRATEGY, STRATEGY_EAGER).equals(STRATEGY_EAGER);
    log.info("# Spam filter initialized.  Temporary ban time " + m_banTime + " mins, max page changes/minute: " + m_limitSinglePageChanges);
}
