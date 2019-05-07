// getRecognizedProperties():String[] 
/** Resets the component. */
public void reset(XMLComponentManager manager) throws XMLConfigurationException {
    // get features 
    fAugmentations = manager.getFeature(AUGMENTATIONS);
    fReportErrors = manager.getFeature(REPORT_ERRORS);
    fNotifyCharRefs = manager.getFeature(NOTIFY_CHAR_REFS);
    fNotifyXmlBuiltinRefs = manager.getFeature(NOTIFY_XML_BUILTIN_REFS);
    fNotifyHtmlBuiltinRefs = manager.getFeature(NOTIFY_HTML_BUILTIN_REFS);
    fFixWindowsCharRefs = manager.getFeature(FIX_MSWINDOWS_REFS);
    fScriptStripCDATADelims = manager.getFeature(SCRIPT_STRIP_CDATA_DELIMS);
    fScriptStripCommentDelims = manager.getFeature(SCRIPT_STRIP_COMMENT_DELIMS);
    fStyleStripCDATADelims = manager.getFeature(STYLE_STRIP_CDATA_DELIMS);
    fStyleStripCommentDelims = manager.getFeature(STYLE_STRIP_COMMENT_DELIMS);
    fIgnoreSpecifiedCharset = manager.getFeature(IGNORE_SPECIFIED_CHARSET);
    fCDATASections = manager.getFeature(CDATA_SECTIONS);
    fOverrideDoctype = manager.getFeature(OVERRIDE_DOCTYPE);
    fInsertDoctype = manager.getFeature(INSERT_DOCTYPE);
    fNormalizeAttributes = manager.getFeature(NORMALIZE_ATTRIBUTES);
    fParseNoScriptContent = manager.getFeature(PARSE_NOSCRIPT_CONTENT);
    // get properties 
    fNamesElems = getNamesValue(String.valueOf(manager.getProperty(NAMES_ELEMS)));
    fNamesAttrs = getNamesValue(String.valueOf(manager.getProperty(NAMES_ATTRS)));
    fDefaultIANAEncoding = String.valueOf(manager.getProperty(DEFAULT_ENCODING));
    fErrorReporter = (HTMLErrorReporter) manager.getProperty(ERROR_REPORTER);
    fDoctypePubid = String.valueOf(manager.getProperty(DOCTYPE_PUBID));
    fDoctypeSysid = String.valueOf(manager.getProperty(DOCTYPE_SYSID));
}
