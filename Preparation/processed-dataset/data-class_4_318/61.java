// checkElementMatchesRootElementDecl  
void reportSchemaError(String key, Object[] arguments) {
    if (fDoValidation)
        fXSIErrorReporter.reportError(XSMessageFormatter.SCHEMA_DOMAIN, key, arguments, XMLErrorReporter.SEVERITY_ERROR);
}
