// scanSurrogates():boolean  
/**
     * Convenience function used in all XML scanners.
     */
protected void reportFatalError(String msgId, Object[] args) throws XNIException {
    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, msgId, args, XMLErrorReporter.SEVERITY_FATAL_ERROR);
}
