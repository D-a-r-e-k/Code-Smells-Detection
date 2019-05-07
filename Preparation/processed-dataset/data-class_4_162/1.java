/**
     *  @param m_engine The WikiEngine this reader is attached to.  Is
     * used to figure out of a page exits.
     */
// FIXME: parsers should be pooled for better performance. 
@SuppressWarnings("unchecked")
private void initialize() {
    PatternCompiler compiler = new GlobCompiler();
    List<Pattern> compiledpatterns;
    // 
    //  We cache compiled patterns in the engine, since their creation is 
    //  really expensive 
    // 
    compiledpatterns = (List<Pattern>) m_engine.getAttribute(INLINE_IMAGE_PATTERNS);
    if (compiledpatterns == null) {
        compiledpatterns = new ArrayList<Pattern>(20);
        Collection ptrns = getImagePatterns(m_engine);
        // 
        //  Make them into Regexp Patterns.  Unknown patterns 
        //  are ignored. 
        // 
        for (Iterator i = ptrns.iterator(); i.hasNext(); ) {
            try {
                compiledpatterns.add(compiler.compile((String) i.next(), GlobCompiler.DEFAULT_MASK | GlobCompiler.READ_ONLY_MASK));
            } catch (MalformedPatternException e) {
                log.error("Malformed pattern in properties: ", e);
            }
        }
        m_engine.setAttribute(INLINE_IMAGE_PATTERNS, compiledpatterns);
    }
    m_inlineImagePatterns = Collections.unmodifiableList(compiledpatterns);
    m_camelCasePattern = (Pattern) m_engine.getAttribute(CAMELCASE_PATTERN);
    if (m_camelCasePattern == null) {
        try {
            m_camelCasePattern = m_compiler.compile(WIKIWORD_REGEX, Perl5Compiler.DEFAULT_MASK | Perl5Compiler.READ_ONLY_MASK);
        } catch (MalformedPatternException e) {
            log.fatal("Internal error: Someone put in a faulty pattern.", e);
            throw new InternalWikiException("Faulty camelcasepattern in TranslatorReader");
        }
        m_engine.setAttribute(CAMELCASE_PATTERN, m_camelCasePattern);
    }
    // 
    //  Set the properties. 
    // 
    Properties props = m_engine.getWikiProperties();
    String cclinks = (String) m_context.getPage().getAttribute(PROP_CAMELCASELINKS);
    if (cclinks != null) {
        m_camelCaseLinks = TextUtil.isPositive(cclinks);
    } else {
        m_camelCaseLinks = TextUtil.getBooleanProperty(props, PROP_CAMELCASELINKS, m_camelCaseLinks);
    }
    Boolean wysiwygVariable = (Boolean) m_context.getVariable(RenderingManager.WYSIWYG_EDITOR_MODE);
    if (wysiwygVariable != null) {
        m_wysiwygEditorMode = wysiwygVariable.booleanValue();
    }
    m_plainUris = getLocalBooleanProperty(m_context, props, PROP_PLAINURIS, m_plainUris);
    m_useOutlinkImage = getLocalBooleanProperty(m_context, props, PROP_USEOUTLINKIMAGE, m_useOutlinkImage);
    m_useAttachmentImage = getLocalBooleanProperty(m_context, props, PROP_USEATTACHMENTIMAGE, m_useAttachmentImage);
    m_allowHTML = getLocalBooleanProperty(m_context, props, MarkupParser.PROP_ALLOWHTML, m_allowHTML);
    m_useRelNofollow = getLocalBooleanProperty(m_context, props, PROP_USERELNOFOLLOW, m_useRelNofollow);
    if (m_engine.getUserManager().getUserDatabase() == null || m_engine.getAuthorizationManager() == null) {
        disableAccessRules();
    }
    m_context.getPage().setHasMetadata();
}
