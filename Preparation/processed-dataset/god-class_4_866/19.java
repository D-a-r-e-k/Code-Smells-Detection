// reset(XMLComponentManager) 
/** Sets a feature. */
public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
    if (featureId.equals(AUGMENTATIONS)) {
        fAugmentations = state;
    } else if (featureId.equals(IGNORE_SPECIFIED_CHARSET)) {
        fIgnoreSpecifiedCharset = state;
    } else if (featureId.equals(NOTIFY_CHAR_REFS)) {
        fNotifyCharRefs = state;
    } else if (featureId.equals(NOTIFY_XML_BUILTIN_REFS)) {
        fNotifyXmlBuiltinRefs = state;
    } else if (featureId.equals(NOTIFY_HTML_BUILTIN_REFS)) {
        fNotifyHtmlBuiltinRefs = state;
    } else if (featureId.equals(FIX_MSWINDOWS_REFS)) {
        fFixWindowsCharRefs = state;
    } else if (featureId.equals(SCRIPT_STRIP_CDATA_DELIMS)) {
        fScriptStripCDATADelims = state;
    } else if (featureId.equals(SCRIPT_STRIP_COMMENT_DELIMS)) {
        fScriptStripCommentDelims = state;
    } else if (featureId.equals(STYLE_STRIP_CDATA_DELIMS)) {
        fStyleStripCDATADelims = state;
    } else if (featureId.equals(STYLE_STRIP_COMMENT_DELIMS)) {
        fStyleStripCommentDelims = state;
    } else if (featureId.equals(IGNORE_SPECIFIED_CHARSET)) {
        fIgnoreSpecifiedCharset = state;
    } else if (featureId.equals(PARSE_NOSCRIPT_CONTENT)) {
        fParseNoScriptContent = state;
    }
}
